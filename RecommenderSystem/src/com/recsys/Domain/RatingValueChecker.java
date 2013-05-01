package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingValueChecker implements Checker<Rating> {

	private double rating;
	
	public RatingValueChecker(double r) {
		super();
		this.rating=r;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return (rating == obj.getRating());
	}

	

}
