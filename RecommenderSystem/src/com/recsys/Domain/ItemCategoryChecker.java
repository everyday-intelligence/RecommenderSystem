package com.recsys.Domain;

import com.recsys.utils.Checker;

public class ItemCategoryChecker implements Checker<Item> {

	private double itemCategory;
	
	public ItemCategoryChecker(double itemCategory) {
		super();
		this.itemCategory=itemCategory;
	}



	@Override
	public boolean verifyPredicate(Item obj) {
		return this.itemCategory == obj.getCategory();
	}

	

}
