package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingUserChecker implements Checker<Rating> {

	private User user;
	
	public RatingUserChecker(User u) {
		super();
		user = u;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return this.user.equals(obj.getRatingUser());
	}

	

}
