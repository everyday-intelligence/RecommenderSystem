package com.recsys.Domain;

public abstract class User {

	private long idUser;

	public User(long idUser) {
		super();
		this.idUser = idUser;
	}

	public long getIdUser() {
		return idUser;
	}

}
