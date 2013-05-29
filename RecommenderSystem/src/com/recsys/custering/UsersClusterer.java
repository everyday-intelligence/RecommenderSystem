package com.recsys.custering;

import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public interface UsersClusterer {
	public List<User> cluster(List<Item> items,List<User> Users, List<Rating> ratings);
}
