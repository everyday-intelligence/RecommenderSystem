package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.recsys.Domain.RatingValueChecker;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
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

public class MyFrequentistModelHard implements
RecommendationStrategy {

	private List<Item> items;
	private List<User> users;
	private List<Rating> ratings;

	private Map<GroupCategory,Double> cacheRatings = new HashMap<GroupCategory,Double>();
	
	public static SimilarityMeasure<Double> pc = new AdjustedCosineSimilarity<Double>();



	public MyFrequentistModelHard(List<User> users,
			List<Item> items, List<Rating> ratings) {
		super();
		this.items = items;
		this.users = users;
		this.ratings = ratings;

	}


	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimationMaxProba(activeUser);
		if (allPossibleCandidatesEstimation == null	|| allPossibleCandidatesEstimation.isEmpty()) {
			List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>();
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
		List<Rating> allRatingsEstimations = new ArrayList<Rating>();
		for (Item it : items) {
			double category = it.getCategory();
			Double estimatedRating = 0d;
			GroupCategory groupCategory = new GroupCategory(group, category);
			estimatedRating = cacheRatings.get(groupCategory);
			if(estimatedRating == null){	
				estimatedRating = 0d;
				Double estimatedRatingProba = 0d;
				Collection<Rating> sachant =  PredicateUtils.findAll(ratings, new CategoryGroupContextChecker(group,category));
				//System.out.println("Group = "+group+" Category = "+category+" sachant = "+sachant.size());
				if(!sachant.isEmpty()){
					for(double rating = 1; rating<=5;rating++){
						Collection<Rating> denum = PredicateUtils.findAll(sachant, new RatingValueChecker(rating));
						Double proba = denum.size()/((double)sachant.size());
						//System.out.println(" rating = "+rating+" proba = "+denum.size()+"/"+sachant.size()+"="+proba);
						if(proba>estimatedRatingProba){
							estimatedRating = rating;
							estimatedRatingProba = proba;
						}
					}
				}
				cacheRatings.put(groupCategory, estimatedRating);
			}
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
		}

		return allRatingsEstimations;
	}

	public List<Rating> userRatingsEstimationExpectation(User activeUser) {
		double group = activeUser.getGroup();
		//System.out.println(group);
		List<Rating> allRatingsEstimations = new ArrayList<Rating>();
		for (Item it : items) {
			double category = it.getCategory();
			Double estimatedRating = 0d;
			GroupCategory groupCategory = new GroupCategory(group, category);
			estimatedRating = cacheRatings.get(groupCategory);
			if(estimatedRating == null){	
				estimatedRating = 0d;
				Collection<Rating> sachant =  PredicateUtils.findAll(ratings, new CategoryGroupContextChecker(group,category));
				if(!sachant.isEmpty()){
					for(double rating = 1; rating<=5;rating++){
						Collection<Rating> denum = PredicateUtils.findAll(sachant, new RatingValueChecker(rating));
						Double proba = denum.size()/((double)sachant.size());
						estimatedRating+=proba*rating;
					}
				}
				estimatedRating = (double) Math.round(estimatedRating);
				cacheRatings.put(groupCategory, estimatedRating);
			}
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
		}

		return allRatingsEstimations;
	}
}
