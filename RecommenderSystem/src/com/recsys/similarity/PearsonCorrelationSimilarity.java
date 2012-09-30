package com.recsys.similarity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.recsys.Domain.User;
import com.recsys.recommendation.Mathematics;

public class PearsonCorrelationSimilarity<Double> extends	NumbersSimilarityMeasure<java.lang.Double> {

	
	public java.lang.Double measureSimilarity(List<java.lang.Double> activeRatings,List<java.lang.Double> otherUserRatings) {
		
		double simPears = 0d;
		double numerateur = 0d;
		double denumerateurUser = 0d;
		double denumerateurActive = 0d;

		if (!activeRatings.isEmpty() && otherUserRatings.size() > 1) {
			double userMeanRatings = Mathematics.average(otherUserRatings);
			double activeMeanRatings = Mathematics.average(activeRatings);	
			
			for (int nb = 0; nb < otherUserRatings.size(); nb++) {
				numerateur += (activeRatings.get(nb) - activeMeanRatings) * (otherUserRatings.get(nb) - userMeanRatings);
				denumerateurUser += Math.pow((activeRatings.get(nb) - activeMeanRatings),2);
				denumerateurActive += Math.pow((otherUserRatings.get(nb) - userMeanRatings),2);
			}

			simPears = numerateur/(Math.sqrt(denumerateurActive*denumerateurUser));
			
		}
		//System.out.println("SPPPPPPPPPPPPPPPP = "+simPears);
		return simPears;

	}

	@Override
	public Boolean isSimilarity() {
		return true;
	}
}