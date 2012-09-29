package com.recsys.similarity;

import java.util.List;

public class InverseManhattanSimilarityNotNullNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		double dist = 0d;
		for(int i=0;i<values1.size();i++){
			if(values1.get(i)*values2.get(i)!= 0){
				dist += Math.abs(values1.get(i)-values2.get(i));				
			}
		}
		return new java.lang.Double(1)/dist;
	}

}
