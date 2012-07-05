package com.recsys.similarity;

import java.util.List;

public abstract class SimilarityMeasure<T> {

	public abstract Double measureSimilarity(List<T> values1, List<T> values2);
}
