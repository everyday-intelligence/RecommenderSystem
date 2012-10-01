package com.recsys.similarity;

import java.util.List;

import com.recsys.matrix.IndexedSimpleMatrix;

public class ManhattanDistanceNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		double dist = 0d;
		for(int i=0;i<values1.size();i++){
			dist += Math.abs(values1.get(i)-values2.get(i));
		}
		if(dist==0){
			return 1d;
		}
		return dist;
	}

	@Override
	public Boolean isSimilarity() {
		return false;
	}

}
