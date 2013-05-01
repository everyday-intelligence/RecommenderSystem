package com.recsys.Domain;

import com.recsys.utils.Checker;

public class RatingItemChecker implements Checker<Rating> {

	private Item item;
	
	public RatingItemChecker(Item item) {
		super();
		this.item=item;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return this.item.equals(obj.getRatedItem());
	}

	

}
