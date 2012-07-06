package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;

public class RecommendationStrategyFactory {

	public static RecommendationStrategy createRecommendationStrategy(){
		//on va lire un fichier de configuretion qui dira quel type de recommendation utiliser pour le moment on teste avec la UserCentered....
		int recommendationType = 0;
		if(recommendationType == 0){
			List<User> users = new ArrayList<User>();
			List<Item> items = new ArrayList<Item>();
			//remplir les listes
			return createUserCenteredCollaborativeFiltering(users, items);
		}
		return null;
	}
	
	private static RecommendationStrategy createUserCenteredCollaborativeFiltering(List<User> users, List<Item> items){
		return new UserCenteredCollaborativeFiltering(users, items);
	}

	
}
