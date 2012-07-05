package com.recsys.similarity;

import java.util.List;

public class InverseManhattanSimilarityNumber<Number> extends
		InverseManhattanSimilarity<Number> {

	@Override
	public Double measureSimilarity(List<Number> values1, List<Number> values2) {
		List<Double> valuesDouble1 = (List<Double>)values1;
		List<Double> valuesDouble2 = (List<Double>)values2;

		Double dist = 0d;
		for(int i=0;i<valuesDouble1.size();i++){
			dist += Math.abs((Double)valuesDouble1.get(i)- (Double)valuesDouble2.get(i));
		}
		System.out.println(dist);
		return new Double(1)/dist;
	}

}
