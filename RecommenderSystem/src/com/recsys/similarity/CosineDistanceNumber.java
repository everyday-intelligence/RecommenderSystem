package com.recsys.similarity;

import java.util.Iterator;
import java.util.List;

public class CosineDistanceNumber<Double> extends NumbersSimilarityMeasure<java.lang.Double> {


	@Override
	public java.lang.Double measureSimilarity(List<java.lang.Double> values1,
			List<java.lang.Double> values2) {
		double dist = 0d;
		double normX = 0d;
		double normY = 0d;
		Iterator<java.lang.Double> i1 = values1.iterator();
		Iterator<java.lang.Double> i2 = values2.iterator();
		while(i1.hasNext()){
			double a1 = i1.next();
			double a2 = i2.next();
			dist += a1*a2;
			normX+=Math.pow(a1,2);
			normY+=Math.pow(a2,2);
		}
		double cosine = dist/(normX*normY);
		//System.out.println(values1.size()+"-------------"+cosine);
		return cosine;
	}

	@Override
	public Boolean isSimilarity() {
		return false;
	}

}
