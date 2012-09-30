package com.recsys.ratingsAggregator;

import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.IndexedSimpleMatrix;

public abstract class RatingAggregator{

	public abstract Double aggregate(User activeUser, Item item, List<User> similarUsers, List<Item> items, Map<User, Double> simMap, IndexedSimpleMatrix dataMatrix);

}
