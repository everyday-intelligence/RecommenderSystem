package com.recsys.similarity;

import java.util.List;

import com.recsys.matrix.IndexedSimpleMatrix;

public class MAEDistanceNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		
		double error = 0d;
		for (int i = 0; i < values1.size(); i++) {
			error += Math.abs(values1.get(i) - values2.get(i));
		}
		
		return error;
	}

	@Override
	public Boolean isSimilarity() {
		return false;
	}

}
