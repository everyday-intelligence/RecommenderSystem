package com.recsys.similarity;

import java.util.List;

import com.recsys.matrix.IndexedSimpleMatrix;

public class RMSEDistanceNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		
		double error = 0d;
		for (int i = 0; i < values1.size(); i++) {
			error += Math.pow(values1.get(i) - values2.get(i),2);
		}
		
		return Math.sqrt(error/values1.size());
	}

	@Override
	public Boolean isSimilarity() {
		return false;
	}

}
