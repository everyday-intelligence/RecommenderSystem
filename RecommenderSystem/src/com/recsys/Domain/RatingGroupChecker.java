package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingGroupChecker implements Checker<Rating> {

	private double group;
	
	public RatingGroupChecker(double g) {
		super();
		group = g;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return group == obj.getRatingUser().getGroup();
	}

	

}
