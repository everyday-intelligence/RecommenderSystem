package com.recsys.similarity;

import java.util.Iterator;
import java.util.List;

import com.recsys.recommendation.Mathematics;

public class AdjustedCosineSimilarity<Double> extends	NumbersSimilarityMeasure<java.lang.Double> {

	
	public java.lang.Double measureSimilarity(List<java.lang.Double> activeRatings,List<java.lang.Double> otherUserRatings) {
		
		double simPears = 0d;
		double numerateur = 0d;
		double denumerateurUser = 0d;
		double denumerateurActive = 0d;

		if (!activeRatings.isEmpty() && otherUserRatings.size() > 1) {
			double userMeanRatings = Mathematics.average(otherUserRatings);
			double activeMeanRatings = Mathematics.average(activeRatings);	
			Iterator<java.lang.Double> i1 = activeRatings.iterator();
			Iterator<java.lang.Double> i2 = otherUserRatings.iterator();
			while(i1.hasNext()){
				double double1 = i1.next();
				double double2 = i2.next();
				numerateur += (double1 - activeMeanRatings) * (double2 - userMeanRatings);
				denumerateurUser += Math.pow((double1 - activeMeanRatings),2);
				denumerateurActive += Math.pow((double2 - userMeanRatings),2);
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