package com.recsys.recommendation;

import java.util.List;

import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;


public interface RecommendationStrategy {//c'est le design pattern strategy

	public List<Recommendation> recommend(User activeUser);
}
