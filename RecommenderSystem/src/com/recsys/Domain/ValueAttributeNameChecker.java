package com.recsys.Domain;

import com.recsys.utils.Checker;

public class ValueAttributeNameChecker implements Checker<AttributeValue> {

	private AttributeValue attributeValue;
	
	public ValueAttributeNameChecker(AttributeValue av) {
		super();
		this.attributeValue=av;
	}



	@Override
	public boolean verifyPredicate(AttributeValue av2) {
		return this.attributeValue.getAttribute().equals(av2.getAttribute());
	}

	

}
