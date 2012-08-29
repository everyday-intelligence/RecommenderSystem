package com.recsys.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public /*abstract*/ class User {
	@Id
	private long idUser;

	public User(){}
	
	public User(long idUser) {
		super();
		this.idUser = idUser;
	}

	public long getIdUser() {
		return idUser;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + "]";
	}

}
