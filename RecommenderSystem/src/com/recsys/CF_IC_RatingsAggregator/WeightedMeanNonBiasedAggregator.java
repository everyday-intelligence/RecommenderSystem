package com.recsys.CF_IC_RatingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;

public class WeightedMeanNonBiasedAggregator extends CF_IC_RatingAggregator{


	private static Map<Item, Double> allItemsMeanRatings = new ConcurrentHashMap<Item, Double>();
	
	public Double aggregate(User user, Item item, IndexedSimpleMatrix itemItemSimilarityMatrix, IndexedSimpleMatrix userItemRatingMatrix, ArrayList<Item> itemNeighborhoodRatedByUser) {
		
		Double itmMeanRatings=allItemsMeanRatings.get(item);
		if (itmMeanRatings  == null) {
			itmMeanRatings = getItemMeanRatings(item, userItemRatingMatrix);
			allItemsMeanRatings.put(item, itmMeanRatings);
		}
		
		Double estimation = 0d;
		double norm = 0d;
		for (Item it : itemNeighborhoodRatedByUser) {
			estimation = estimation	+ (userItemRatingMatrix.get(user.getIdUser(), it.getIdItem()) - getItemMeanRatings(it, userItemRatingMatrix))
					* itemItemSimilarityMatrix.get(item.getIdItem(),it.getIdItem());
			norm += itemItemSimilarityMatrix.get(item.getIdItem(),it.getIdItem());
		}
		if(!estimation.isInfinite()&&estimation != 0){
			estimation/=norm;
		}
		return estimation + itmMeanRatings;
	}
	
	double getItemMeanRatings(Item itm, IndexedSimpleMatrix userItemRatingMatrix){
		double itmMeanRating = 0d;
		int nbItmRatings = 0;
		AbstractVector allItmRatings = userItemRatingMatrix.getColumn(itm.getIdItem());
		for(int i=0;i<allItmRatings.length;i++){
			if(allItmRatings.get(i)!=0){
				itmMeanRating+=1;
				nbItmRatings++;
			}
		}
		if(nbItmRatings!=0){
			itmMeanRating/=nbItmRatings;
		}
		return itmMeanRating;
	}
}