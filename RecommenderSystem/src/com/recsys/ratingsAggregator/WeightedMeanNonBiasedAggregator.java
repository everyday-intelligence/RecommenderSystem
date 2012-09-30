package com.recsys.ratingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.Mathematics;

public class WeightedMeanNonBiasedAggregator extends RatingAggregator {

	private static User activeUser;
	private static double activeMeanRatings = 0d;
	private static List<Double> similarsMeanRatings = new ArrayList<Double>();

	@Override
	public Double aggregate(User activeUser, Item it, List<User> similarUsers,
			List<Item> items, Map<User, Double> simMap,
			IndexedSimpleMatrix dataMatrix) {


		double estimation = 0;
		if (dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
	
			if(this.activeUser==null || !activeUser.equals(this.activeUser)){
				this.activeUser = activeUser;
				int nbActiveRatings = 0;
				for (Item itm : items) {
					Double ruItm = dataMatrix.get(activeUser.getIdUser(),
							itm.getIdItem());
					if (ruItm != 0) {
						activeMeanRatings += ruItm;
						nbActiveRatings++;
					}
				}
				if(nbActiveRatings==0){
					activeMeanRatings = 0;
				}else{
					activeMeanRatings = activeMeanRatings / nbActiveRatings;					
				}

				for (User user : similarUsers) {
					double meanURatings = 0d;
					int nbURatings = 0;
					for (Item itm : items) {
						Double ruItm = dataMatrix.get(user.getIdUser(),	itm.getIdItem());
						if (ruItm != 0) {
							meanURatings += ruItm;
							nbURatings++;
						}
					}
					if(nbActiveRatings==0){
						similarsMeanRatings.add(0d);
					}else{
						similarsMeanRatings.add(meanURatings / nbURatings);
					}
				}
			}

			
			/*Estim*/
			List<Double> similarsCommonRatings = new ArrayList<Double>();
			List<Double> similarsSimilarityValues = new ArrayList<Double>();
			for (User user : similarUsers) {
				if (dataMatrix.get(user.getIdUser(), it.getIdItem()) != 0) {
					similarsCommonRatings.add(dataMatrix.get(user.getIdUser(),it.getIdItem()));
					similarsSimilarityValues.add(simMap.get(user));
				}
			}

			double avg = 0;
			double norm = 0;
			for (int i = 0; i < similarsCommonRatings.size(); i++) {
				avg += Math.abs(similarsCommonRatings.get(i) - similarsMeanRatings.get(i))*similarsSimilarityValues.get(i);
				norm += similarsSimilarityValues.get(i);
			}
			if(avg==0){
				return 0d;
			}
			estimation= activeMeanRatings + (avg / norm);

		}
		return estimation;
	}
}
