package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;

import com.recsys.CF_IC_RatingsAggregator.CF_IC_RatingAggregator;
import com.recsys.CF_IC_RatingsAggregator.MeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanNonBiasedAggregator;
import com.recsys.Domain.CategoryGroupContextChecker;
import com.recsys.Domain.GroupCategory;
import com.recsys.Domain.GroupCategoryRating;
import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.RatingGroupChecker;
import com.recsys.Domain.RatingUserCategoryChecker;
import com.recsys.Domain.RatingUserChecker;
import com.recsys.Domain.RatingValueChecker;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.Domain.UserCategory;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.similarity.AdjustedCosineSimilarity;
import com.recsys.similarity.CosineDistanceNumber;
import com.recsys.similarity.MAEDistanceNumber;
import com.recsys.similarity.ManhattanDistanceNumber;
import com.recsys.similarity.RMSEDistanceNumber;
import com.recsys.similarity.SimilarityMeasure;
import com.recsys.utils.PredicateUtils;

public class MyFrequentistModelHard implements RecommendationStrategy {

	private List<Item> items;
	// private List<User> users;
	private List<Rating> ratings;

	private Map<GroupCategory, Double> cacheRatings = new Hashtable<GroupCategory, Double>();
	private Map<GroupCategory, Double> cacheGroupCategoryMeanRating = new Hashtable<GroupCategory, Double>();
	private Map<UserCategory, Double> cacheUserCategoryMeanRating = new Hashtable<UserCategory, Double>();
	private AbstractMatrix userItemRatingMatrix;
	private static Map<User, Double> cacheAllUsersMeanRatings = new ConcurrentHashMap<User, Double>();
	private static Map<Double, Double> cacheAllGroupsMeanRatings = new ConcurrentHashMap<Double, Double>();

	public MyFrequentistModelHard(List<User> users, List<Item> items,
			List<Rating> ratings) {
		super();
		this.items = items;
		// this.users = users;
		this.ratings = ratings;
		this.userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			if(r == null){System.out.println(r+" is null");}
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
	}
	public MyFrequentistModelHard(List<User> users, List<Item> items,
			List<Rating> ratings, AbstractMatrix matrix) {
		super();
		this.items = items;
		// this.users = users;
		this.ratings = ratings;
		this.userItemRatingMatrix = matrix;
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimationMaxProba(activeUser);
		if (allPossibleCandidatesEstimation == null
				|| allPossibleCandidatesEstimation.isEmpty()) {
			List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>(
					allPossibleCandidatesEstimation.size());
			for (Rating r : allPossibleCandidatesEstimation) {
				allPossibleCandidates.add(new Recommendation(r.getRatedItem(),
						r.getRating()));
			}
			return allPossibleCandidates;
		} else {
			// TODO si on veut recommander les plus populaires
			return null;
		}
	}

	public List<Rating> userRatingsEstimationMaxProba(User activeUser) {
		double group = activeUser.getGroup();
		List<Rating> allRatingsEstimations = new ArrayList<Rating>(items.size());
		for (Item it : items) {
			double category = it.getCategory();
			Double estimatedRating = 0d;
			GroupCategory groupCategory = new GroupCategory(group, category);
			estimatedRating = cacheRatings.get(groupCategory);
			if (estimatedRating == null) {
				estimatedRating = 0d;
				Double estimatedRatingProba = 0d;
				Collection<Rating> sachant = PredicateUtils.findAll(ratings,
						new CategoryGroupContextChecker(group, category));
				// System.out.println("Group = "+group+" Category = "+category+" sachant = "+sachant.size());
				if (!sachant.isEmpty()) {
					for (double rating = 1; rating <= 5; rating++) {
						Collection<Rating> denum = PredicateUtils.findAll(
								sachant, new RatingValueChecker(rating));
						Double proba = denum.size() / ((double) sachant.size());
						// System.out.println(" rating = "+rating+" proba = "+denum.size()+"/"+sachant.size()+"="+proba);
						if (proba > estimatedRatingProba) {
							estimatedRating = rating;
							estimatedRatingProba = proba;
						}
					}
				}
				cacheRatings.put(groupCategory, estimatedRating);
			}
			allRatingsEstimations.add(new Rating(estimatedRating, it,
					activeUser));
		}

		return allRatingsEstimations;
	}

	public List<Rating> userRatingsEstimationExpectation(User activeUser) {
		double group = activeUser.getGroup();
		// System.out.println(group);
		List<Rating> allRatingsEstimations = new ArrayList<Rating>(items.size());
		for (Item it : items) {
			double category = it.getCategory();
			Double estimatedRating = 0d;
			GroupCategory groupCategory = new GroupCategory(group, category);
			estimatedRating = cacheRatings.get(groupCategory);
			if (estimatedRating == null) {
				estimatedRating = 0d;
				Collection<Rating> sachant = PredicateUtils.findAll(ratings,
						new CategoryGroupContextChecker(group, category));
				if (!sachant.isEmpty()) {
					for (double rating = 1; rating <= 5; rating++) {
						Collection<Rating> denum = PredicateUtils.findAll(
								sachant, new RatingValueChecker(rating));
						Double proba = denum.size() / ((double) sachant.size());
						estimatedRating += proba * rating;
					}
				}
				// estimatedRating = (double) Math.round(estimatedRating);
				cacheRatings.put(groupCategory, estimatedRating);
			}
			allRatingsEstimations.add(new Rating(estimatedRating, it,
					activeUser));
		}

		return allRatingsEstimations;
	}
	public List<Rating> userRatingsEstimationExpectationNonBiased(User activeUser) {
		 List<Rating> biasedRatings = userRatingsEstimationExpectation(activeUser);
		List<Rating> allRatingsEstimations = new ArrayList<Rating>(items.size());
		 for(Rating r:biasedRatings){
			//double ucmr = getUserCategoryMeanRatings(activeUser,r.getRatedItem().getCategory());
			//double gcmr = getGroupCategoryMeanRatings(activeUser.getGroup(), r.getRatedItem().getCategory());
			//System.out.println("ucmr="+ucmr+" gcmr"+gcmr);
			//double estim = r.getRating() - gcmr+ucmr;
			 double umr = getUserMeanRatings(activeUser);
			 Double gmr = getGroupMeanRatings(activeUser.getGroup());
			//System.out.println("biased "+r.getRating()+" nonbiased"+estim);
			 double estim = r.getRating() - gmr+umr;
			r.setRating(estim);	
			allRatingsEstimations.add(r);
		}
		return allRatingsEstimations;
	}
	
	private Double getGroupCategoryMeanRatings(double group,double category) {
		GroupCategory groupCategory = new GroupCategory(group, category);
		Double gpCatMeanRating = cacheGroupCategoryMeanRating.get(groupCategory);
		if (gpCatMeanRating == null) {
			Collection<Rating> grpCatRatings = PredicateUtils.findAll(ratings,new CategoryGroupContextChecker(group, category));
			gpCatMeanRating = 0d;
			if(!grpCatRatings.isEmpty()){
				for(Rating r:grpCatRatings){
					gpCatMeanRating+=r.getRating();
				}
				gpCatMeanRating/=grpCatRatings.size();
			}
			cacheGroupCategoryMeanRating.put(groupCategory, gpCatMeanRating);
		}
		return gpCatMeanRating;
	}
	
	
	
	private double getUserMeanRatings(User user){		
		Double meanURatings = cacheAllUsersMeanRatings.get(user);
		if (meanURatings==null) {
			meanURatings=0d;
			Collection<Rating> uRatings = PredicateUtils.findAll(ratings,new RatingUserChecker(user));
			if(!uRatings.isEmpty()){
				for(Rating r:uRatings){
					meanURatings+=r.getRating();
				}
				meanURatings/=uRatings.size();	
			}
		}
		cacheAllUsersMeanRatings.put(user, meanURatings);
		//System.out.println(hit/nbCalls+"%");
		return meanURatings;
	}
	private Double getGroupMeanRatings(double group) {
		Double gMeanRAtings = cacheAllGroupsMeanRatings.get(group);
		if (gMeanRAtings==null) {
			gMeanRAtings=0d;
			Collection<Rating> gRatings = PredicateUtils.findAll(ratings,new RatingGroupChecker(group));
			if(!gRatings.isEmpty()){
				for(Rating r:gRatings){
					gMeanRAtings+=r.getRating();
				}
				gMeanRAtings/=gRatings.size();	
			}
		}
		cacheAllGroupsMeanRatings.put(group, gMeanRAtings);
		//System.out.println(hit/nbCalls+"%");
		return gMeanRAtings;
	}
	private double getUserCategoryMeanRatings(User user,double category){
		UserCategory userCategory = new UserCategory(user.getIdUser(), category);
		Double meanUCatRatings = cacheUserCategoryMeanRating.get(userCategory);
		if(meanUCatRatings == null){
			meanUCatRatings = 0d;
			Collection<Rating> uCatRatings = PredicateUtils.findAll(ratings,new RatingUserCategoryChecker(user.getIdUser(), category));
			if(!uCatRatings.isEmpty()){
				for(Rating r:uCatRatings){
					meanUCatRatings+=r.getRating();
				}
				meanUCatRatings/=uCatRatings.size();	
			}else{
				meanUCatRatings = getUserMeanRatings(user);
			}
			cacheUserCategoryMeanRating.put(userCategory, meanUCatRatings);
		}
		return meanUCatRatings;
	}
}
