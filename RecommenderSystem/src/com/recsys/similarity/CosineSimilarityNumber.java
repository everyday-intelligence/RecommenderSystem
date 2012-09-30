package com.recsys.similarity;

import java.util.List;

public class CosineSimilarityNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		double dist = 0d;
		double normX = 0d;
		double normY = 0d;
		for(int i=0;i<values1.size();i++){
			dist += values1.get(i)*values2.get(i);
			normX+=Math.pow(values1.get(i),2);
			normY+=Math.pow(values2.get(i),2);
		}
		return (dist/(normX*normY));
	}

	@Override
	public Boolean isSimilarity() {
		return false;
	}

}
