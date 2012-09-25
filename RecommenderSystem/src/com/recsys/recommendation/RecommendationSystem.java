package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;

public class RecommendationSystem {//on verra ce qui est manque

	private RecommendationStrategy algorithm;
	private int recommendationsNumber = 10;
	private User activeUser;
	private List<Recommendation> allPotentialRecommendations = new ArrayList<Recommendation>();	
	
	
	
	public RecommendationSystem() {
		super();
		//this.algorithm = RecommendationStrategyFactory.createRecommendationStrategy();
		//lire le fichier de configuration contenant le nombre de recommendations. par defaut c'est 10.
	}

	public void calculateRecomendations(){
		this.allPotentialRecommendations = algorithm.recommend(activeUser); 
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
		
}
