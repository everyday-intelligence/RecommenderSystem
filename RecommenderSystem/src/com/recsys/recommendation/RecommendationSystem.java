package com.recsys.recommendation;

public class RecommendationSystem {

	private RecommendationStrategy algorithm;

	public RecommendationSystem(RecommendationStrategy algorithm) {
		super();
		this.algorithm = algorithm;
	}

	public RecommendationStrategy getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(RecommendationStrategy algorithm) {
		this.algorithm = algorithm;
	}
	
	
}
