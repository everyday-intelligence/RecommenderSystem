package com.recsys.similarity;

import java.util.List;

import com.recsys.recommendation.Mathematics;

public class PearsonCorrelation<Double> extends	NumbersSimilarityMeasure<java.lang.Double> {

	public java.lang.Double measureSimilarity(List<java.lang.Double> activeRatings,List<java.lang.Double> otherUserRatings) {
		double simPears = 0d;
		if (!activeRatings.isEmpty() && otherUserRatings.size() > 1) {
			for (int nb = 0; nb < otherUserRatings.size(); nb++) {
				simPears += (activeRatings.get(nb) * Mathematics.average(activeRatings))
						* (otherUserRatings.get(nb) * Mathematics
								.average(otherUserRatings));
			}

			simPears /= (otherUserRatings.size()* Mathematics.standardDeviation(activeRatings)
					* Mathematics.standardDeviation(otherUserRatings));
		}
		//System.out.println("SPPPPPPPPPPPPPPPP = "+simPears);
		return simPears;

	}

	@Override
	public Boolean isSimilarity() {
		return true;
	}
}