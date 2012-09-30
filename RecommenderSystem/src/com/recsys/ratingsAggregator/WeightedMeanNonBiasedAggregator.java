package com.recsys.ratingsAggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.Mathematics;

public class WeightedMeanNonBiasedAggregator extends RatingAggregator {


	private static Map<User, Double> allUsersMeanRatings = new ConcurrentHashMap<User, Double>();

	@Override
	public Double aggregate(User activeUser, Item it, List<User> similarUsers,
			List<Item> items, Map<User, Double> simMap,
			IndexedSimpleMatrix dataMatrix) {

		double estimation = 0;
		if (dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {

			Double activeMeanRatings=allUsersMeanRatings.get(activeUser);
			if (activeMeanRatings  == null) {
				activeMeanRatings = 0d;
				int nbActiveRatings = 0;
				for (Item itm : items) {
					Double ruItm = dataMatrix.get(activeUser.getIdUser(),
							itm.getIdItem());
					if (ruItm != 0) {
						activeMeanRatings += ruItm;
						nbActiveRatings++;
					}
				}
				if (nbActiveRatings == 0) {
					activeMeanRatings = 0d;
				} else {
					activeMeanRatings = activeMeanRatings / nbActiveRatings;
				}
				allUsersMeanRatings.put(activeUser, activeMeanRatings);
			}

			for (User user : similarUsers) {
				if (!allUsersMeanRatings.containsKey(user)) {
					double meanURatings = 0d;
					int nbURatings = 0;
					for (Item itm : items) {
						Double ruItm = dataMatrix.get(user.getIdUser(),
								itm.getIdItem());
						if (ruItm != 0) {
							meanURatings += ruItm;
							nbURatings++;
						}
					}
					if (nbURatings == 0) {
						allUsersMeanRatings.put(user, 0d);
					} else {
						allUsersMeanRatings.put(user, meanURatings
								/ nbURatings);
					}
				}
			}

			/* Estim */

			double avg = 0;
			double norm = 0;

			for (User user : similarUsers) {
				Double r_u_it = dataMatrix.get(user.getIdUser(), it.getIdItem());
				if (r_u_it != 0) {
					avg +=simMap.get(user)*(r_u_it - allUsersMeanRatings.get(user));
					norm += simMap.get(user);
				}
			}
			if (avg == 0) {
				return 0d;
			}
			estimation = activeMeanRatings + (avg / norm);

		}
		return estimation;
	}
}
