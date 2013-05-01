package com.recsys.Domain;

import com.recsys.utils.Checker;

public class UserGroupChecker implements Checker<User> {

	private double userGroup;
	
	public UserGroupChecker(double userGroup) {
		super();
		this.userGroup=userGroup;
	}



	@Override
	public boolean verifyPredicate(User obj) {
		return this.userGroup == obj.getGroup();
	}

	

}
