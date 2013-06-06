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

public class MyFrequentistModelSoft implements RecommendationStrategy {

	private List<Item> items;
	private List<User> users;
	private List<Rating> ratings;

	private Map<GroupCategoryRating, Double> cacheRatingsProbas = new HashMap<GroupCategoryRating, Double>();
	private int nbGps;
	private int nbCts;


	public MyFrequentistModelSoft(List<User> users, List<Item> items,List<Rating> ratings,int nbgs,int nbcts) {
		super();
		this.items = items;
		this.users = users;
		this.ratings = ratings;
		this.nbCts = nbcts;
		this.nbGps=nbgs;
		groupCategoryRatingsEstimation();
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimationExpectation(activeUser);
		if (allPossibleCandidatesEstimation == null
				|| allPossibleCandidatesEstimation.isEmpty()) {
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

	public List<Rating> userRatingsEstimationExpectation(User activeUser) {
		//System.out.println("estimate for user"+activeUser.getIdUser());
		List<Rating> allRatingsEstimations = new ArrayList<Rating>();
		for (Item it : items) {
			Double estimatedRating = 0d;
			for (int gp=0;gp<nbGps;gp++) {
				Double p_us_gi = activeUser.getGroupsMemberships()[gp];
				for (int ct=0;ct<nbCts;ct++) {
					Double p_it_ci = it.getCategoriesMemberships()[ct];
					for(double r = 1; r<=5;r++){
						GroupCategoryRating groupCategoryRating = new GroupCategoryRating(gp, ct, r);
						Double p_ei_gi_ct_ctx = cacheRatingsProbas.get(groupCategoryRating);
						if(p_ei_gi_ct_ctx!=null){//normalement toujours vrai
							estimatedRating+=p_ei_gi_ct_ctx*r*p_it_ci*p_us_gi;
							//System.out.println(estimatedRating);
						}else{
						}
					}
				}
			}
			//System.out.println(estimatedRating);
			//estimatedRating = (double) Math.round(estimatedRating);
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
		}

		return allRatingsEstimations;
	}
	public void groupCategoryRatingsEstimation() {
		// System.out.println(group);
		for (int gp=0;gp<nbGps;gp++) {
			for (int ct=0;ct<nbCts;ct++) {
				for (double r = 1; r <= 5; r++) {
					GroupCategoryRating groupCategoryRating = new GroupCategoryRating(gp, ct, r);
					Collection<Rating> sachant = PredicateUtils.findAll(ratings, new CategoryGroupContextChecker(gp,ct));
					if (!sachant.isEmpty()) {
						Collection<Rating> denum = PredicateUtils.findAll(sachant, new RatingValueChecker(r));
						Double proba = denum.size() / ((double) sachant.size());
						cacheRatingsProbas.put(groupCategoryRating, proba);
						//System.out.println("p("+r+"|g"+gp+",c"+ct+"="+proba);
					}
				}
			}
		}
	}
}
