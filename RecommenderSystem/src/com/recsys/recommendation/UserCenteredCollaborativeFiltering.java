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
import com.recsys.ratingsAggregator.MeanAggregator;
import com.recsys.ratingsAggregator.RatingAggregator;
import com.recsys.ratingsAggregator.WeightedMeanAggregator;
import com.recsys.ratingsAggregator.WeightedMeanNonBiasedAggregator;
import com.recsys.similarity.CosineDistanceNumber;
import com.recsys.similarity.MAEDistanceNumber;
import com.recsys.similarity.ManhattanDistanceNumber;
import com.recsys.similarity.RMSEDistanceNumber;
import com.recsys.similarity.SimilarityMeasure;

public class UserCenteredCollaborativeFiltering implements
		RecommendationStrategy {

	private List<User> users = new ArrayList<User>();
	private List<Item> items = new ArrayList<Item>();
	private IndexedSimpleMatrix dataMatrix;
	// Top-K neighbor, threashold, notRated: value for unrated items
	public static final int K = 40;
	public static final int NOTRATED = 0;
	SimilarityMeasure<Double> pc = new CosineDistanceNumber<Double>();
	RatingAggregator na = new WeightedMeanNonBiasedAggregator();

	public UserCenteredCollaborativeFiltering(List<User> users,List<Item> items, List<Rating> ratings) {
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
		List<Rating> allPossibleCandidatesEstimation = ratingEstimation(activeUser, similarUsersList,simMap);
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
					if(!activeUserRatings.isEmpty()){//no commons
						simPears = pc.measureSimilarity(activeUserRatings,userRatings);
						if (!Double.isInfinite(simPears) && !Double.isNaN(simPears)) {
							//simPears=0;
							//System.out.println("SimPearson User"+this.users.get(row).getIdUser()+" = "+simPears);
							simMap.put(u, simPears);
						}
					}
					// if similarity is infinite: there is no proof of
					// similarity
					
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
		if(pc.isSimilarity()){
			Collections.reverse(simList);
		}
		double threashold = simList.get(Math.min(simList.size()-1, K));	
		
		//System.out.println("user "+activeUser.getIdUser()+" sims vary from "+Collections.min(simList)+" to "+Collections.max(simList)+ " simMin = "+threashold);
		ArrayList<User> neighborList = new ArrayList<User>();
		for(User u:simMap.keySet()){
			if(pc.isSimilarity()){
				if(simMap.get(u)>=threashold){
					neighborList.add(u);
					//System.out.println(simMap.get(u)+" taken");
				}
			}else{
				if(simMap.get(u)<=threashold){
					neighborList.add(u);
					//System.out.println(simMap.get(u)+" taken");
				}
			}
			
		}
		if(neighborList.isEmpty()){
		System.out.println("!!!!!!!!!!!!!!!!"+neighborList.size());
		try {
			this.wait(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return neighborList;
	}
	
	// Rating estimation
	public List<Rating> ratingEstimation(User activeUser,
			ArrayList<User> similarUserList, Map<User, Double> simMap) {

		double estimation;
		List<Rating> allPossibleCandidatesEstimations = new ArrayList<Rating>();

		if (similarUserList==null || similarUserList.isEmpty()) {
			System.out.println("No estimation");
			return new ArrayList<Rating>();
		}
		
		List<Double> activeRatings = new ArrayList<Double>();
		for (Item it:items) {
			activeRatings.add(this.dataMatrix.get(activeUser.getIdUser(), it.getIdItem()));
		}
		for (Item it:items) {
			estimation = 0;
			if (this.dataMatrix.get(activeUser.getIdUser(), it.getIdItem()) == 0) {
				estimation = na.aggregate(activeUser,it,similarUserList,items,simMap,dataMatrix);
				if (estimation != 0) {
					allPossibleCandidatesEstimations.add(new Rating(estimation,	it, activeUser));
				}
			}
		}
		return allPossibleCandidatesEstimations;

	}
	
	
}
