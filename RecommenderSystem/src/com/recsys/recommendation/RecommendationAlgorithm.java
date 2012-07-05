package com.recsys.recommendation;

import java.util.List;

import com.recsys.Domain.User;


public interface RecommendationAlgorithm {

	public List<Recommendation> recommend(User activeUser);
}
