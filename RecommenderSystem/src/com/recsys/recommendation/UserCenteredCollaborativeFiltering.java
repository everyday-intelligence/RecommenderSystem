package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.MatrixFactory;

public class UserCenteredCollaborativeFiltering implements
		RecommendationStrategy {

	private List<User> users = new ArrayList<User>();
	private List<Item> items = new ArrayList<Item>();
	private AbstractMatrix dataMatrix;
	private Map<Long,Integer> userIDMatrixMapping;
	private Map<Long,Integer> itemIDMatrixMapping;

	// Top-K neighbor, threashold, notRated: value for unrated items
	public static final int K = 150;

	public static final int NOTRATED = 0;

	public UserCenteredCollaborativeFiltering(List<User> users,
			List<Item> items, List<Rating> ratings) {
		super();
		this.users = users;
		this.items = items;
		dataMatrix = MatrixFactory.createMatrix(users.size(), items.size());
		System.out.println("rating matrix : "+dataMatrix.getRowsNumber()+"x"+dataMatrix.getColumnsNumber());

		userIDMatrixMapping = new HashMap<Long,Integer>(users.size());
		for(int i=0;i<users.size();i++){userIDMatrixMapping.put(users.get(i).getIdUser(),i);}
		itemIDMatrixMapping = new HashMap<Long,Integer>(items.size());
		for(int i=0;i<items.size();i++){itemIDMatrixMapping.put(items.get(i).getIdItem(),i);}
		
		
		for (Rating r : ratings) {
			dataMatrix.set(fromRealUserIdToMatrixID(r.getRatingUser().getIdUser()),
					fromRealItemIdToMatrixID(r.getRatedItem().getIdItem()),
					r.getRating());
		}
		
		/*
		for (Rating entry : ratings) {
			dataMatrix.set((int) entry.getRatingUser().getIdUser() - 1,
					(int) entry.getRatedItem().getIdItem() - 1,
					entry.getRating());
		}
		*/
	}

	public AbstractMatrix getDataMatrix() {
		return this.dataMatrix;
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {

		// similarity Map
		Map<User, Double> simMap = simPearson(activeUser);
		// user list array
		ArrayList<User> similarUsersList = neighborhood(simMap, activeUser);
		List<Rating> allPossibleCandidatesEstimation = ratingEstimation(
				activeUser, similarUsersList);
		List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>();
		for (Rating r : allPossibleCandidatesEstimation) {
			allPossibleCandidates.add(new Recommendation(r.getRatedItem(), r
					.getRating()));
		}
		return allPossibleCandidates;
	}

	// ici les méthodes de recherche de voisinage, estimation, ...... .....

	// Similarity: Pearson's correlation coefficient
	public Map<User, Double> simPearson(User activeUser) {

		double simPears = 0;

		Map<User, Double> simMap = new HashMap<User, Double>();
		ArrayList<Double> user = new ArrayList<Double>();
		ArrayList<Double> activeList = new ArrayList<Double>();
		// looking for common items

		for (int row = 0; row < this.dataMatrix.getRowsNumber(); row++) {

			if (row != fromRealUserIdToMatrixID(activeUser.getIdUser())) {

				for (int col = 0; col < this.dataMatrix.getColumnsNumber(); col++) {
					Double userRowItemColRating = this.dataMatrix.get(row, col);
					Double activeUserItemColRating = this.dataMatrix.get(fromRealUserIdToMatrixID(activeUser.getIdUser()), col);
					if ((userRowItemColRating != NOTRATED)&& (activeUserItemColRating != NOTRATED)) {
						activeList.add(activeUserItemColRating);
						user.add(userRowItemColRating);
						//System.out.println("both users "+activeUser.getIdUser()+" and user "+fromMatrixUserIdToRealID(row) +" rated Item "+fromMatrixItemIdToRealID(col));
					}else{
						//System.out.println("both users "+activeUser.getIdUser()+" and user "+fromMatrixUserIdToRealID(row) +" have not rated Item "+fromMatrixItemIdToRealID(col));
					}
				}
				// calculates Pearson's correlation coefficient -
				// simPearson(activeUser,allOtherUsers)
				if (!activeList.isEmpty() && user.size() > 1) {

					for (int nb = 0; nb < user.size(); nb++) {

						simPears += (activeList.get(nb) * Mathematics
								.average(activeList))
								* (user.get(nb) * Mathematics.average(user));

					}

					simPears /= (user.size() - 1)
							* Mathematics.standardDeviation(activeList)
							* Mathematics.standardDeviation(user);
					// if similarity is infinite: there is no proof of
					// similarity
					if (simPears != Double.NEGATIVE_INFINITY
							&& simPears != Double.POSITIVE_INFINITY) {
						// simPears=0;
						// System.out.println("SimPearson User"+this.users.get(row).getIdUser()+" = "+simPears);
						simMap.put(this.users.get(row), simPears);
					} else {
						//System.out.println("SimPearson User"+this.users.get(row).getIdUser() + " = 0");
					}
				}

				user.clear();
				activeList.clear();
				simPears = 0;

			}
		}

		return simMap;

	}

	// Looking for Neighborhood
	public ArrayList<User> neighborhood(Map<User, Double> simMap,
			User activeUser) {

		// Similarity List
		ArrayList<Double> simList = new ArrayList<Double>(simMap.values());
		// Users List
		ArrayList<User> userList = new ArrayList<User>(simMap.keySet());
		ArrayList<User> neighborList = new ArrayList<User>();

		int col = 0;

		if (simMap.isEmpty()) {

			System.out.println("There is no neighbors");
			System.exit(0);
		}

		while (this.dataMatrix.get(fromRealUserIdToMatrixID(activeUser.getIdUser()), col) != NOTRATED) {

			col++;

		}

		// Sorting the similarity list to get top neighborhood
		while (!userList.isEmpty()) {

			double max = simList.get(0);
			int user = 0;

			for (int i = 1; i < simList.size(); i++) {

				// comparing simList values
				if (max < simList.get(i)) {
					max = simList.get(i);
					user = i;
				} else { // if similarity is equal
					if (max == simList.get(i)) {
						// comparing dataMatrix value to find the top neighbor
						if (this.dataMatrix.get(
								fromRealUserIdToMatrixID(activeUser.getIdUser()), col) < this.dataMatrix
								.get(fromRealUserIdToMatrixID(activeUser.getIdUser()), col)) {
							max = simList.get(i);
							user = i;
						}
					}

				}

			}

			//TODO
			neighborList.add(this.users.get(this.users.indexOf(userList.get(user))));

			userList.remove(user);
			simList.remove(user);

		}

		// Top-k neighborhood

		neighborList = new ArrayList<User>(neighborList.subList(0,Math.min(K, neighborList.size())));
		return neighborList;

	}

	// Rating estimation
	public List<Rating> ratingEstimation(User activeUser,
			ArrayList<User> similarUserList) {

		double estimation;
		List<Rating> allPossibleCandidatesEstimations = new ArrayList<Rating>();
		int card = 0;

		if (similarUserList.isEmpty()) {
			System.out.println("No estimation");
			System.exit(0);
		}
		int indexOfActiveUser = fromRealUserIdToMatrixID(activeUser.getIdUser());
		for (int col = 0; col < this.dataMatrix.getColumnsNumber(); col++) {
			estimation = 0;
			if (this.dataMatrix.get(indexOfActiveUser, col) == 0) {
				for (User user : similarUserList) {
					if (this.dataMatrix.get(fromRealUserIdToMatrixID(user.getIdUser()), col) != 0) {
						estimation += this.dataMatrix.get(fromRealUserIdToMatrixID(user.getIdUser()), col);
						card++;
					}
				}
				if (estimation != 0) {
					estimation /= card;
					allPossibleCandidatesEstimations.add(new Rating(estimation,	this.items.get(col), activeUser));
				}
				card = 0;
			}
		}
		return allPossibleCandidatesEstimations;

	}
	
	int fromRealUserIdToMatrixID(long idUser){
		return userIDMatrixMapping.get(idUser);		
	}
	int fromMatrixUserIdToRealID(int userRowIndex){
		return (int) users.get(userRowIndex).getIdUser();		
	}
	int fromRealItemIdToMatrixID(long idItem){
		return itemIDMatrixMapping.get(idItem);		
	}
	int fromMatrixItemIdToRealID(int itemColIndex){
		return (int) items.get(itemColIndex).getIdItem();		
	}
}
