package com.recsys.CF_UC_RatingsAggregator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;

public class WeightedMeanNonBiasedAggregator extends CF_UC_RatingAggregator {


	private static Map<User, Double> allUsersMeanRatings = new ConcurrentHashMap<User, Double>();
	private static double hit = 0;
	private static double nbCalls = 0;

	@Override
	public Double aggregate(User activeUser, Item it, List<User> similarUsers,
			List<Item> items, Map<User, Double> simMap,	AbstractMatrix dataMatrix) {
		double estimation = 0;
		if (dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
			double activeMeanRatings=getUserMeanRatings(activeUser, items, dataMatrix);

			/* Estim */

			double avg = 0;
			double norm = 0;

			for (User user : similarUsers) {
				Double r_u_it = dataMatrix.get(user.getIdUser(), it.getIdItem());
				if (r_u_it != 0) {
					Double simUserActiveUser = simMap.get(user);
					avg +=simUserActiveUser*(r_u_it - getUserMeanRatings(user, items, dataMatrix));
					norm += simUserActiveUser;
				}
			}
			if (avg != 0) {
				estimation = activeMeanRatings + (avg / norm);
			}
		}
		return estimation;
	}
	private double getUserMeanRatings(User user,List<Item> items,AbstractMatrix dataMatrix){
		nbCalls++;
		
		Double meanURatings = allUsersMeanRatings.get(user);
		if (meanURatings==null) {
			meanURatings = 0d;
			int nbURatings = 0;
			
				
				AbstractVector userAllRatings = dataMatrix.getRow(user.getIdUser());
				for(int i=0;i<userAllRatings.size();i++){
					double ruItm = userAllRatings.get(i);
					if (ruItm != 0) {
						meanURatings += ruItm;
						nbURatings++;
					}
				}
			if (nbURatings == 0) {
				allUsersMeanRatings.put(user, 0d);
			} else {
				meanURatings /= nbURatings;
				allUsersMeanRatings.put(user, meanURatings);
			}
		}else{
			hit++;
		}
		//System.out.println(hit/nbCalls+"%");
		return meanURatings;
	}
}
