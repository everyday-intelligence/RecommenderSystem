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
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.similarity.PearsonCorrelation;

public class UserCenteredCollaborativeFiltering implements
		RecommendationStrategy {

	private List<User> users = new ArrayList<User>();
	private List<Item> items = new ArrayList<Item>();
	private IndexedSimpleMatrix dataMatrix;
	// Top-K neighbor, threashold, notRated: value for unrated items
	public static final int K = 150;
	public static final int NOTRATED = 0;
	PearsonCorrelation pc = new PearsonCorrelation();


	public UserCenteredCollaborativeFiltering(List<User> users,
			List<Item> items, List<Rating> ratings) {
		super();
		this.users = users;
		this.items = items;
		this.dataMatrix = MatrixFactory.createMatrix(users, items);
		System.out.println("rating matrix : "+dataMatrix.getRowsNumber()+"x"+dataMatrix.getColumnsNumber());
		for (Rating r : ratings) {
			if(r == null){System.out.println(r+" is null");}
			dataMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
	}



	@Override
	public List<Recommendation> recommend(User activeUser) {

		// similarity Map
		Map<User, Double> simMap = calculateUsersSimilarities(activeUser);
		// user list array
		ArrayList<User> similarUsersList = neighborhood(simMap, activeUser);
		List<Rating> allPossibleCandidatesEstimation = ratingEstimation(
				activeUser, similarUsersList);
		if(allPossibleCandidatesEstimation == null || allPossibleCandidatesEstimation.isEmpty()){
			List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>();
			for (Rating r : allPossibleCandidatesEstimation) {
				allPossibleCandidates.add(new Recommendation(r.getRatedItem(), r
						.getRating()));
			}
			return allPossibleCandidates;
		}else{
			//TODO si on veut recommander les plus populaires
			return null;
		}
		
	}

	// ici les méthodes de recherche de voisinage, estimation, ...... .....

	// Similarity: Pearson's correlation coefficient
	public Map<User, Double> calculateUsersSimilarities(User activeUser) {


		Map<User, Double> simMap = new HashMap<User, Double>();
		ArrayList<Double> userRatings = new ArrayList<Double>();
		ArrayList<Double> activeUserRatings = new ArrayList<Double>();
		// looking for common items
		for (User u:users) {
			double simPears = 0d;
			if (u.getIdUser() != activeUser.getIdUser()) {
				for (Item it:items) {
					Double userRowItemColRating = this.dataMatrix.get(u.getIdUser(), it.getIdItem());
					Double activeUserItemColRating = this.dataMatrix.get(activeUser.getIdUser(), it.getIdItem());
					if ((userRowItemColRating != NOTRATED) && (activeUserItemColRating != NOTRATED)) {
						activeUserRatings.add(activeUserItemColRating);
						userRatings.add(userRowItemColRating);
						//System.out.println("both users "+activeUser.getIdUser()+" and user "+fromMatrixUserIdToRealID(row) +" rated Item "+fromMatrixItemIdToRealID(col));
					}else{
						//System.out.println("both users "+activeUser.getIdUser()+" and user "+fromMatrixUserIdToRealID(row) +" have not rated Item "+fromMatrixItemIdToRealID(col));
					}
				}
				// calculates Pearson's correlation coefficient -
				// simPearson(activeUser,allOtherUsers)
				simPears = pc.measureSimilarity(activeUserRatings,userRatings);
					// if similarity is infinite: there is no proof of
					// similarity
					if (simPears != Double.NEGATIVE_INFINITY
							&& simPears != Double.POSITIVE_INFINITY) {
						// simPears=0;
						// System.out.println("SimPearson User"+this.users.get(row).getIdUser()+" = "+simPears);
						simMap.put(u, simPears);
					} else {
						//System.out.println("SimPearson User"+this.users.get(row).getIdUser() + " = 0");
					}
				}

				userRatings.clear();
				activeUserRatings.clear();
				simPears = 0;

			}
		

		return simMap;
	}
	

	public ArrayList<User> neighborhood(Map<User, Double> simMap,
			User activeUser) {
		
		if (simMap.isEmpty()) {
			System.out.println("There is no neighbors");
			return null;
		}
		
		ArrayList<Double> simList = new ArrayList<Double>(simMap.values());
		Collections.sort(simList);
		double simMin = simList.get(Math.min(simList.size()-1, K));		
		ArrayList<User> neighborList = new ArrayList<User>();
		for(User u:simMap.keySet()){
			if(simMap.get(u)>simMin){
				neighborList.add(u);
			}
		}
		return neighborList;
	}
	
	
	/*
	
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
			return null;
		}

		while (this.dataMatrix.getByLabel(activeUser.getIdUser(), col) != NOTRATED) {
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
						if (this.dataMatrix.getByLabel(
								activeUser.getIdUser(), col) < this.dataMatrix
								.getByLabel(activeUser.getIdUser(), col)) {
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
*/
	// Rating estimation
	public List<Rating> ratingEstimation(User activeUser,
			ArrayList<User> similarUserList) {

		double estimation;
		List<Rating> allPossibleCandidatesEstimations = new ArrayList<Rating>();
		int card = 0;

		if (similarUserList==null || similarUserList.isEmpty()) {
			System.out.println("No estimation");
			return new ArrayList<Rating>();
		}
		for (Item it:items) {
			estimation = 0;
			if (this.dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
				for (User user : similarUserList) {
					if (this.dataMatrix.get(user.getIdUser(), it.getIdItem()) != 0) {
						estimation += this.dataMatrix.get(user.getIdUser(), it.getIdItem());
						card++;
					}
				}
				if (estimation != 0) {
					estimation /= card;
					allPossibleCandidatesEstimations.add(new Rating(estimation,	it, activeUser));
				}
				card = 0;
			}
		}
		return allPossibleCandidatesEstimations;

	}
	
	
}
