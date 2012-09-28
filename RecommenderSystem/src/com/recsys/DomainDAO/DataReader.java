package com.recsys.DomainDAO;

import java.util.List;
import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public interface DataReader {
		public List<User> findUsersFile(String fichier);
		public List<Item> findItemsFile(String fichier);
		public List<Rating> findRatingsFile(String fichier);
		public List<Rating> findUserRatings(String fichier, long l);
}
