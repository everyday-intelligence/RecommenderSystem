package com.recsys.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity 
public class Rating implements Serializable{
	
	@Id
	private long idRating;
	
	private double rating;
	@OneToOne(fetch=FetchType.LAZY)
	private Item ratedItem;
	@OneToOne(fetch=FetchType.LAZY)
	private User ratingUser;
	
	public Rating(){}
	
	public Rating(double rating,Item ratedItem,User ratingUser){
		this.rating=rating;
		this.ratedItem=ratedItem;
		this.ratingUser=ratingUser;
	}
	
	public double getRating() {
		return rating;
	}
	
	public Item getRatedItem() {
		return ratedItem;
	}
	
	public User getRatingUser() {
		return ratingUser;
	}
	
	@Override
	public String toString() {
		return "User [idUser=" + ratingUser +" idItem= "+ ratedItem +" rating= "+rating + "]";
	}
	

}

