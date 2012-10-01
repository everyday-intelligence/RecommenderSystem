package com.recsys.recommendation;

import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public class RecommendationStrategyFactory {

	public static RecommendationStrategy createRecommendationStrategy(List<User> users, List<Item> items, List<Rating> ratings){
		//on va lire un fichier de configuretion qui dira quel type de recommendation utiliser pour le moment on teste avec la UserCentered....
		int recommendationType = 0;
		if(recommendationType == 0){
			return createUserCenteredCollaborativeFiltering(users, items, ratings);
		}
		return null;
	}
	
	private static RecommendationStrategy createUserCenteredCollaborativeFiltering(List<User> users, List<Item> items, List<Rating> ratings){
		return new UserCenteredCollaborativeFiltering(users, items,ratings);
	}

	
}
