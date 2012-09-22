package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;

public class KnowledgeBasedFiltering implements RecommendationStrategy {

	private List<User> users = new ArrayList<User>();
	//private List<classe de règles> lesreglesapprises = new ArrayList<classe de règles>();
	//private Double seuil de probabilité 
	
	//constructeurs paramétré qui initialise les règles en appelant la méthode d'apprentissage des règles
	
	@Override
	public List<Recommendation> recommend(User activeUser) {
		// apprendre les règles
		//générer des recommandations pour l'utilisateur actuel.
		return null;
	}
	//méthode d'apprentissage des regles

}
