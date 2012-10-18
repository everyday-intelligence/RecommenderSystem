package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.recsys.CF_IC_RatingsAggregator.CF_IC_RatingAggregator;
import com.recsys.CF_IC_RatingsAggregator.MeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanNonBiasedAggregator;
import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.similarity.AdjustedCosineSimilarity;
import com.recsys.similarity.CosineDistanceNumber;
import com.recsys.similarity.MAEDistanceNumber;
import com.recsys.similarity.ManhattanDistanceNumber;
import com.recsys.similarity.RMSEDistanceNumber;
import com.recsys.similarity.SimilarityMeasure;

public class ItemCenteredCollaborativeFiltering implements
		RecommendationStrategy {

	private List<Item> items;
	private IndexedSimpleMatrix userItemRatingMatrix;
	private IndexedSimpleMatrix itemItemSimilarityMatrix;
	public static final int K = 300;

	SimilarityMeasure<Double> pc = new CosineDistanceNumber<Double>();
	CF_IC_RatingAggregator na = new MeanAggregator();

	public ItemCenteredCollaborativeFiltering(List<User> users,
			List<Item> items, List<Rating> ratings) {
		super();
		this.items = items;
		this.userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		System.out.println("rating matrix : "
				+ userItemRatingMatrix.getRowsNumber() + "x"
				+ userItemRatingMatrix.getColumnsNumber());
		System.out.println("filling ratings");
		for (Rating r : ratings) {
			if (r == null) {
				System.out.println(r + " is null");
			}
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(), r
					.getRatedItem().getIdItem(), r.getRating());
		}
		System.out.println("calculating item item similarities");
		calculateItemsSimilarities();
		System.out.println("finished calculating item item similarities");
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimation(activeUser);
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

	public List<Rating> userRatingsEstimation(User activeUser) {
		List<Rating> allRatingsEstimations = new ArrayList<Rating>();
		for (Item it : items) {
			ArrayList<Item> itemNeighborhoodRatedByUser = itemNeighborhoodRatedByUser(activeUser, it);
			Double estimatedRating = na.aggregate(activeUser, it, itemItemSimilarityMatrix, userItemRatingMatrix, itemNeighborhoodRatedByUser );
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
		}

		return allRatingsEstimations;
	}
	
	
	public ArrayList<Item> itemNeighborhoodRatedByUser(User user, Item item) {
		ArrayList<Item> itemsRatedByUser = new ArrayList<Item>();
		List<Double> itemRatedSimilarityValues = new ArrayList<Double>();

		for(Item it:items){
			if(userItemRatingMatrix.get(user.getIdUser(), it.getIdItem())!=0){
				itemsRatedByUser.add(it);
				itemRatedSimilarityValues.add(itemItemSimilarityMatrix.get(item.getIdItem(), it.getIdItem()));
			}
		}
		
		Collections.sort(itemRatedSimilarityValues);
		if (pc.isSimilarity()) {
			Collections.reverse(itemRatedSimilarityValues);
		}
		double threashold = itemRatedSimilarityValues.get(Math.min(
				itemRatedSimilarityValues.size() - 1, K));
		ArrayList<Item> neighborList = new ArrayList<Item>();

		for (Item itm : itemsRatedByUser) {
			if (!itemItemSimilarityMatrix.get(item.getIdItem(), itm.getIdItem()).isNaN()) {
				if (pc.isSimilarity()) {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) >= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				} else {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) <= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				}
			}
		}
		return neighborList;
	}

	
	public ArrayList<Item> neighborhood__(Item item) {
		List<Double> itemSimilarityValues = new ArrayList<Double>();
		AbstractVector row = itemItemSimilarityMatrix.getRow(item.getIdItem());
		for (int i = 0; i < row.length; i++) {
			itemSimilarityValues.add(row.get(i));
		}
		Collections.sort(itemSimilarityValues);
		if (pc.isSimilarity()) {
			Collections.reverse(itemSimilarityValues);
		}
		double threashold = itemSimilarityValues.get(Math.min(
				itemSimilarityValues.size() - 1, K));
		ArrayList<Item> neighborList = new ArrayList<Item>();

		for (Item itm : items) {
			if (!itemItemSimilarityMatrix.get(item.getIdItem(), itm.getIdItem()).isNaN()) {
				if (pc.isSimilarity()) {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) >= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				} else {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) <= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				}
			}
		}

		return neighborList;
	}

	public void calculateItemsSimilarities() {
		this.itemItemSimilarityMatrix = MatrixFactory.createMatrix(items);
		for (Item it1 : items) {
			for (Item it2 : items) {
				if (!it1.equals(it2) && this.itemItemSimilarityMatrix.get(it1.getIdItem(),it2.getIdItem()) == 0) {
				double it1it2Sim = calculateTwoItesmSimilarities(it1, it2);
				itemItemSimilarityMatrix.set(it1.getIdItem(), it2.getIdItem(),it1it2Sim);
				itemItemSimilarityMatrix.set(it2.getIdItem(), it1.getIdItem(),it1it2Sim);
				//System.out.println("sim(" + it1.getIdItem() + ","	+ it2.getIdItem() + ") =" + it1it2Sim);
			}
			}
		}
	}

	private double calculateTwoItesmSimilarities(Item it1, Item it2) {
		if (!it1.equals(it2)) {
			AbstractVector it1Ratings = this.userItemRatingMatrix.getColumn(it1
					.getIdItem());
			AbstractVector it2Ratings = this.userItemRatingMatrix.getColumn(it2
					.getIdItem());
			// cosine
			List<Double> it1CommonRatings = new ArrayList<Double>();
			List<Double> it2CommonRatings = new ArrayList<Double>();
			for (int i = 0; i < it1Ratings.length; i++) {
				if (it1Ratings.get(i) != 0 && it1Ratings.get(i) != 0) {
					it1CommonRatings.add(it1Ratings.get(i));
					it2CommonRatings.add(it2Ratings.get(i));
				}
			}
			if (it1CommonRatings.isEmpty()) {
				return Double.NEGATIVE_INFINITY;
			}
			return pc.measureSimilarity(it1CommonRatings, it2CommonRatings);
		} else {
			return Double.NEGATIVE_INFINITY;
		}
	}
	
	

}
