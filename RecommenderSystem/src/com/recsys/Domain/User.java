package com.recsys.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public /*abstract*/ class User implements Serializable {
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
