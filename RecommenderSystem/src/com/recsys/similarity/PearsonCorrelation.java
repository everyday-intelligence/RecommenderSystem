package com.recsys.similarity;

import java.util.List;

import com.recsys.recommendation.Mathematics;

public class PearsonCorrelation<Double> extends	NumbersSimilarityMeasure<java.lang.Double> {

	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,List<java.lang.Double> values2) {

		double simPears = 0d;
		if (!values1.isEmpty() && values2.size() > 1) {
			for (int nb = 0; nb < values2.size(); nb++) {
				simPears += (values1.get(nb) * Mathematics
						.average(values1))
						* (values2.get(nb) * Mathematics
								.average(values2));
			}

			simPears /= (values2.size() - 1)
					* Mathematics.standardDeviation(values1)
					* Mathematics.standardDeviation(values2);
		}
		return simPears;

	}

	@Override
	public Boolean isSimilarity() {
		return true;
	}
}