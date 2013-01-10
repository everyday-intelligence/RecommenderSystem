package com.recsys.CF_IC_RatingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;

public class WeightedMeanAggregator  extends CF_IC_RatingAggregator{

	public Double aggregate(User user, Item item, IndexedSimpleMatrix itemItemSimilarityMatrix, IndexedSimpleMatrix userItemRatingMatrix, ArrayList<Item> itemNeighborhoodRatedByUser) {

		//ArrayList<Item> itemNeighborhoodRatedByUser = itemNeighborhoodRatedByUser(user,item);
		//System.out.println("item "+item.getIdItem()+" has "+itemNeighborhoodRatedByUser.size()+"  rated by user");
		
		Double estimation = 0d;
		double norm = 0d;
		for (Item it : itemNeighborhoodRatedByUser) {
			estimation = estimation	+ userItemRatingMatrix.get(user.getIdUser(), it.getIdItem())* itemItemSimilarityMatrix.get(item.getIdItem(),it.getIdItem());
			//System.out.println(estimation+"+"+ userItemRatingMatrix.get(user.getIdUser(), it.getIdItem())+"*"+ itemItemSimilarityMatrix.get(item.getIdItem(),it.getIdItem()));
			norm += itemItemSimilarityMatrix.get(item.getIdItem(),it.getIdItem());
		}
		if(estimation.isInfinite()||estimation == 0){
			//System.out.println("estim = "+estimation+" norm = "+norm);
			return 0d;
		}else{
			//System.out.println(estimation / norm);
		}
		return estimation / norm;
	}
}
