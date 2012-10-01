package com.recsys.similarity;

import java.util.List;

import com.recsys.matrix.IndexedSimpleMatrix;

public abstract class SimilarityMeasure<T> {

	public abstract Double measureSimilarity(List<T> values1, List<T> values2);
	public abstract Boolean isSimilarity();

}
