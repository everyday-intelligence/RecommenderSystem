package com.recsys.CF_UC_RatingsAggregator;

import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.IndexedSimpleMatrix;

public abstract class CF_UC_RatingAggregator{

	public abstract Double aggregate(User activeUser, Item item, List<User> similarUsers, List<Item> items, Map<User, Double> simMap, AbstractMatrix dataMatrix);

}
