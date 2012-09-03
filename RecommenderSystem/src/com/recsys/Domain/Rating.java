package com.recsys.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity 
public class Rating implements Serializable{
	
	@Id
	private int rating;
	@OneToOne(fetch=FetchType.LAZY)
	private Item ratedItem;
	@OneToOne(fetch=FetchType.LAZY)
	private User ratingUser;
	
	public Rating(){}
	
	public Rating(int rating,Item ratedItem,User ratingUser){
		this.rating=rating;
		this.ratedItem=ratedItem;
		this.ratingUser=ratingUser;
	}
	
	public int getRating() {
		return rating;
	}
	
	@Override
	public String toString() {
		return "User [idUser=" + ratingUser +" idItem= "+ ratedItem +" rating= "+rating + "]";
	}
	

}

