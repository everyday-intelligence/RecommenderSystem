package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;

public class KnowledgeBasedFiltering implements RecommendationStrategy {

	private List<User> users = new ArrayList<User>();
	//private List<classe de r�gles> lesreglesapprises = new ArrayList<classe de r�gles>();
	//private Double seuil de probabilit� 
	
	//constructeurs param�tr� qui initialise les r�gles en appelant la m�thode d'apprentissage des r�gles
	
	@Override
	public List<Recommendation> recommend(User activeUser) {
		// apprendre les r�gles
		//g�n�rer des recommandations pour l'utilisateur actuel.
		return null;
	}
	//m�thode d'apprentissage des regles

}
