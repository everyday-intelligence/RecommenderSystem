package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingUserCategoryChecker implements Checker<Rating> {

	private double userId;
	private double category;
	
	



	public RatingUserCategoryChecker(double userId, double category) {
		super();
		this.userId = userId;
		this.category = category;
	}





	@Override
	public boolean verifyPredicate(Rating r) {
		return this.userId == r.getRatingUser().getIdUser() && this.category==r.getRatedItem().getCategory();
	}

	

}
