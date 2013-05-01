package com.recsys.Domain;

import com.recsys.utils.Checker;

public class CategoryGroupContextChecker implements Checker<Rating> {

	private double group;
	private double category;
	//private Date? context;
	
	public CategoryGroupContextChecker(double g, double c) {
		super();
		this.group=g;
		this.category = c;
	}



	@Override
	public boolean verifyPredicate(Rating obj) {
		return (category == obj.getRatedItem().getCategory())&&(group == obj.getRatingUser().getGroup());
		//TODO add context
	}

	

}
