package com.recsys.custering;

import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;

public class GreedyItemsUsersBiClusterer implements ItemsUsersBiClusterer {

	private List<Item> items;
	private List<User> users;
	private IndexedSimpleMatrix userItemRatingMatrix;
	
	public GreedyItemsUsersBiClusterer(List<Item> items,List<User> users, List<Rating> ratings) {
		super();
		this.items = items;
		this.users = users;
		userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
	}

	@Override
	public void cluster() {
		// TODO Auto-generated method stub

	}
	
	public double measure() {
		double m =0;
		for(int i=0;i<userItemRatingMatrix.getRowsNumber();i++){
			for(int j=1;j<userItemRatingMatrix.getColumnsNumber()-1;j++){
				double mij = 0d;
				int n=0;
				//mij
				if(i>0){
					//m
				}
			}
		}
		return m;
	}

	public List<Item> getItems() {
		return items;
	}

	public List<User> getUsers() {
		return users;
	}

}
