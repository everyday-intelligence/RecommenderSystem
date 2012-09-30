package com.recsys.ratingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.Mathematics;

public class WeightedMeanAggregator extends RatingAggregator {


	@Override
	public Double aggregate(User activeUser, Item it,List<User> similarUsers, List<Item> items,Map<User, Double> simMap,IndexedSimpleMatrix dataMatrix) {
		

		
		double estimation = 0;
		if (dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
			List<Double> similarsCommonRatings = new ArrayList<Double>();
			List<Double> similarsSimilarityValues = new ArrayList<Double>();
			for (User user : similarUsers) {
				if (dataMatrix.get(user.getIdUser(), it.getIdItem()) != 0) {
					similarsCommonRatings.add(dataMatrix.get(user.getIdUser(), it.getIdItem()));
					similarsSimilarityValues.add(simMap.get(user));
				}
			}		
		
		double avg = 0;
		double norm = 0;
		for (int i=0;i<similarsCommonRatings.size();i++) {
			avg += similarsCommonRatings.get(i)*similarsSimilarityValues.get(i);
			norm+=similarsSimilarityValues.get(i);
		}
		if(avg!=0){
			estimation= avg/norm;
		}
	}
		
		return estimation;
	}
	

}
