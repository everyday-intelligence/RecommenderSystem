package com.recsys.CF_IC_RatingsAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.IndexedSimpleMatrix;

public abstract class CF_IC_RatingAggregator{

	public abstract Double aggregate(User user, Item item, AbstractMatrix itemItemSimilarityMatrix, AbstractMatrix userItemRatingMatrix, ArrayList<Item> itemNeighborhoodRatedByUser);

}
