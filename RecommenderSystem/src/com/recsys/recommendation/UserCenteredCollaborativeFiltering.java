package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.MatrixFactory;

public class UserCenteredCollaborativeFiltering implements RecommendationStrategy  {


	private List<User> users = new ArrayList<User>();
	private List<Item> items = new ArrayList<Item>();
	private AbstractMatrix dataMatrix;
	
	public UserCenteredCollaborativeFiltering(List<User> users, List<Item> items) {
		super();
		this.users = users;
		this.items = items;
		dataMatrix = MatrixFactory.createMatrix(users.size(), items.size());
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {
		// créer le voisinage
		//estimer
		//prédire les notes
		//retourner toutes les recommandations (tout les produits notés)
		return null;
	}
	
	//ici les méthodes de recherche de voisinage, estimation, ...... .....
	

}
