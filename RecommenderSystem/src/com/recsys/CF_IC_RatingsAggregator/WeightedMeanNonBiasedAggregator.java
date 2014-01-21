package com.recsys.CF_IC_RatingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;

public class WeightedMeanNonBiasedAggregator extends CF_IC_RatingAggregator{


	private static Map<Item, Double> allItemsMeanRatings = new ConcurrentHashMap<Item, Double>();
	
	public Double aggregate(User user, Item item, AbstractMatrix itemItemSimilarityMatrix, AbstractMatrix userItemRatingMatrix, ArrayList<Item> itemNeighborhoodRatedByUser) {
		
		Double itmMeanRatings=getItemMeanRatings(item, userItemRatingMatrix);
		
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
	
	double getItemMeanRatings(Item itm, AbstractMatrix userItemRatingMatrix){
		Double itmMeanRating=allItemsMeanRatings.get(itm);
		if (itmMeanRating  != null) {
			return itmMeanRating;
		}
		itmMeanRating = 0d;
		int nbItmRatings = 0;
		AbstractVector allItmRatings = userItemRatingMatrix.getColumn(itm.getIdItem());
		for(int i=0;i<allItmRatings.length;i++){
			Double itmR = allItmRatings.get(i);
			if(itmR!=0){
				itmMeanRating+=itmR;
				nbItmRatings++;
			}
		}
		if(nbItmRatings!=0){
			itmMeanRating/=nbItmRatings;
		}
		allItemsMeanRatings.put(itm, itmMeanRating);
		return itmMeanRating;
	}
}
