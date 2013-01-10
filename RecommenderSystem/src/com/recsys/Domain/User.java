package com.recsys.Domain;

import java.io.Serializable;
import java.util.ArrayList;
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
	private double group; 
	private List<AttributeValue> attributesValues;

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

	
	public void setIdUser(long idUser) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idUser ^ (idUser >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (idUser != other.idUser)
			return false;
		return true;
	}

	public double getGroup() {
		return group;
	}

	public void setGroup(double group) {
		this.group = group;
	}

	public List<AttributeValue> getAttributesValues() {
		return attributesValues;
	}

	public void setAttributesValues(List<AttributeValue> attributesValues) {
		this.attributesValues = attributesValues;
	}
	public void addAttributeValue(AttributeValue attributeValue) {
		if(this.attributesValues == null){
			this.attributesValues = new ArrayList<AttributeValue>();
		}
		this.attributesValues.add(attributeValue);
	}

}
