package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;
import com.recsys.Domain.User;

public class RecommendationSystem {

	private RecommendationStrategy algorithm;
	private int recommendationsNumber = 10;
	private User activeUser;
	private List<Recommendation> allPotentialRecommendations = new ArrayList<Recommendation>();	
	
	
	
	public void calculateRecomendations(){
		this.allPotentialRecommendations = algorithm.recommend(activeUser); 
	}
	
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

	public int getRecommendationsNumber() {
		return recommendationsNumber;
	}

	public void setRecommendationsNumber(int recommendationsNumber) {
		this.recommendationsNumber = recommendationsNumber;
	}

	public User getActiveUser() {
		return activeUser;
	}

	public List<Recommendation> getAllPotentialRecommendations() {
		return allPotentialRecommendations;
	}
	
	public List<Recommendation> getBestPotentialRecommendations() {
		if(allPotentialRecommendations.isEmpty()){
			calculateRecomendations();
		}
		return allPotentialRecommendations.subList(0, recommendationsNumber);
	}
	
	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}
	
	
}
