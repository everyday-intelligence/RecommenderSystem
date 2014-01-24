package com.recsys.CF_UC_RatingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.Mathematics;

public class MeanAggregator extends CF_UC_RatingAggregator {
	@Override
	public Double aggregate(User activeUser, Item it, List<User> similarUsers,List<Item> items,
			Map<User, Double> simMap, AbstractMatrix dataMatrix) {

		double estimation = 0;
		List<Double> similarsCommonRatings = new ArrayList<Double>();
		if (dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
			for (User user : similarUsers) {
				Double userItRating = dataMatrix.get(user.getIdUser(),it.getIdItem());
				if (userItRating != 0) {
					similarsCommonRatings.add(userItRating);
				}
			}
			 estimation= Mathematics.average(similarsCommonRatings);
		}
		
		if(estimation == 0){
			//System.out.println(similarsCommonRatings);
		}

		return estimation;
	}
}
