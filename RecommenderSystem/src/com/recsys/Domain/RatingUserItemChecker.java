package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingUserItemChecker implements Checker<Rating> {

	private Rating rating;
	
	public RatingUserItemChecker(Rating rating) {
		super();
		this.rating=rating;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return this.rating.getRatedItem().equals(obj.getRatedItem())&& this.rating.getRatingUser().equals(obj.getRatingUser());
	}

	

}
