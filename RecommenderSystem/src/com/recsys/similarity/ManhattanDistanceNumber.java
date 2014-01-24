package com.recsys.similarity;

import java.util.Iterator;
import java.util.List;

public class ManhattanDistanceNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		double dist = 0d;
		Iterator<java.lang.Double> i1 = values1.iterator();
		Iterator<java.lang.Double> i2 = values2.iterator();
		while(i1.hasNext()){
			dist += Math.abs(i1.next()-i2.next());
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
