package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RecommendationItemIDChecker implements Checker<Recommendation> {

	private long itemId;
	
	public RecommendationItemIDChecker(long itemId) {
		super();
		this.itemId = itemId;
	}



	@Override
	public boolean verifyPredicate(Recommendation obj) {
		return obj.getRecommendedItem().getIdItem() == itemId;
	}

	

}
