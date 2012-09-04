package com.recsys.Domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="users")
public /*abstract*/ class User implements Serializable {
	@Id
	private long idUser;
	@OneToMany(fetch=FetchType.EAGER,mappedBy="ratingUser")
	private List<Rating> ratings;
	
	//Joint table Rating
	@OneToOne(fetch=FetchType.LAZY,mappedBy="ratingUser")
	@JoinTable(
		      name="Rating",
		      joinColumns={@JoinColumn(name="ratingUser_idUser", referencedColumnName="idUser")},
		      inverseJoinColumns={@JoinColumn(name="RatedItem_idItem", referencedColumnName="ratedItem_idItem")})
	
	private Rating rate;
	
	public User(){}
	
	public User(long idUser) {
		super();
		this.idUser = idUser;
	}

	public long getIdUser() {
		return idUser;
	}
	
	public List<Rating> getRatingList() {
		return ratings;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + "]";
	}

}
