package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;

import com.recsys.CF_IC_RatingsAggregator.CF_IC_RatingAggregator;
import com.recsys.CF_IC_RatingsAggregator.MeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanAggregator;
import com.recsys.CF_IC_RatingsAggregator.WeightedMeanNonBiasedAggregator;
import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.similarity.AdjustedCosineSimilarity;
import com.recsys.similarity.CosineDistanceNumber;
import com.recsys.similarity.MAEDistanceNumber;
import com.recsys.similarity.ManhattanDistanceNumber;
import com.recsys.similarity.RMSEDistanceNumber;
import com.recsys.similarity.SimilarityMeasure;

public class DemographicFiltering implements
		RecommendationStrategy {

	private List<Item> items;
	private List<User> users;
	private AbstractMatrix userItemRatingMatrix;
	private AbstractMatrix itemItemSimilarityMatrix;
	private AbstractMatrix userUserSimilarityMatrix;

	public AbstractMatrix getUserUserSimilarityMatrix() {
		return userUserSimilarityMatrix;
	}

	public void setUserUserSimilarityMatrix(
			IndexedSimpleMatrix userUserSimilarityMatrix) {
		this.userUserSimilarityMatrix = userUserSimilarityMatrix;
	}

	public static int getKitems() {
		return KItems;
	}

	public static int getKusers() {
		return KUsers;
	}



	public static final int KItems = 5;
	public static final int KUsers = 5;
	
	public static SimilarityMeasure<Double> pc = new AdjustedCosineSimilarity<Double>();

	
	public DemographicFiltering(List<Item> items,List<User> users, AbstractMatrix userItemRatingMatrix, AbstractMatrix itemItemSimilarityMatrix,AbstractMatrix userUserSimilarityMatrix) {
		super();
		this.items = items;
		this.users = users;
		this.userItemRatingMatrix = userItemRatingMatrix;
		if(itemItemSimilarityMatrix == null){
			itemItemSimilarityMatrix = calculateItemsSimilarities();			
			System.out.println("finished calculating item item similarities 2");
		}
		this.itemItemSimilarityMatrix = itemItemSimilarityMatrix;			
		if(userUserSimilarityMatrix == null){
			userUserSimilarityMatrix = calculateUsersSimilarities();			
			System.out.println("finished calculating user user similarities 2");
		}
		this.userUserSimilarityMatrix = userUserSimilarityMatrix;			
	}
	
	public DemographicFiltering(List<User> users,
			List<Item> items, List<Rating> ratings) {
		super();
		this.items = items;
		this.userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			if (r == null) {
				System.out.println(r + " is null");
			}
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(), r
					.getRatedItem().getIdItem(), r.getRating());
		}
		System.out.println("calculating item item similarities");
		itemItemSimilarityMatrix = calculateItemsSimilarities();
		System.out.println("finished calculating item item similarities");
		System.out.println("calculating user user similarities");
		userUserSimilarityMatrix = calculateUsersSimilarities();
		System.out.println("finished calculating user user similarities");
	}

	
	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimation(activeUser);
		if (allPossibleCandidatesEstimation == null	|| allPossibleCandidatesEstimation.isEmpty()) {
			List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>();
			for (Rating r : allPossibleCandidatesEstimation) {
				allPossibleCandidates.add(new Recommendation(r.getRatedItem(),
						r.getRating()));
			}
			return allPossibleCandidates;
		} else {
			// TODO si on veut recommander les plus populaires
			return null;
		}
	}

	public List<Rating> userRatingsEstimation(User activeUser) {
		ArrayList<User> similarUsers = userNeighborhood(activeUser);
		List<Rating> allRatingsEstimations = new ArrayList<Rating>();
		for (Item it : items) {
			ArrayList<Item> similarItems = itemNeighborhood(it);
			Double estimatedRating = 0d;
			double norm = 0;
			for(User u:similarUsers){
				for(Item sItem:similarItems){
					Double simUsSimItRating = userItemRatingMatrix.get(u.getIdUser(), sItem.getIdItem());
					if(simUsSimItRating!=0){
						Double uSim = userUserSimilarityMatrix.get(activeUser.getIdUser(), u.getIdUser());
						Double itSim = itemItemSimilarityMatrix.get(it.getIdItem(), sItem.getIdItem());
						estimatedRating+=simUsSimItRating * uSim * itSim;
						norm+=uSim*itSim;
					}					
				}
			}
			if(!estimatedRating.isInfinite()&&estimatedRating != 0){
				estimatedRating/= norm;
			}
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
			//System.out.println("-------------------------"+norm);
		}

		return allRatingsEstimations;
		
	}
	
	public ArrayList<Item> itemNeighborhood(Item activeItem) {
		List<Double> similaritiesWithActiveItem = itemItemSimilarityMatrix.getRow(activeItem.getIdItem()).toList();
		
		Collections.sort(similaritiesWithActiveItem);
		if (pc.isSimilarity()) {
			Collections.reverse(similaritiesWithActiveItem);
		}
		double threashold = similaritiesWithActiveItem.get(Math.min(
				similaritiesWithActiveItem.size() - 1, KItems));
		
		ArrayList<Item> neighborList = new ArrayList<Item>();

		for (Item itm : items) {
			if (!itemItemSimilarityMatrix.get(activeItem.getIdItem(), itm.getIdItem()).isNaN()) {
				if (pc.isSimilarity()) {
					if (itemItemSimilarityMatrix.get(activeItem.getIdItem(),
							itm.getIdItem()) >= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				} else {
					if (itemItemSimilarityMatrix.get(activeItem.getIdItem(),
							itm.getIdItem()) <= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				}
			}
		}
		return neighborList;
	}

	
	public ArrayList<User> userNeighborhood(User activeUser) {
		List<Double> similaritiesWithActiveUser = userUserSimilarityMatrix.getRow(activeUser.getIdUser()).toList();
		Collections.sort(similaritiesWithActiveUser);
		if (pc.isSimilarity()) {
			Collections.reverse(similaritiesWithActiveUser);
		}
		double threashold = similaritiesWithActiveUser.get(Math.min(
				similaritiesWithActiveUser.size() - 1, KUsers));
		
		ArrayList<User> neighborList = new ArrayList<User>();

		for (User itm : users) {
			if (!userUserSimilarityMatrix.get(activeUser.getIdUser(), itm.getIdUser()).isNaN()) {
				if (pc.isSimilarity()) {
					if (userUserSimilarityMatrix.get(activeUser.getIdUser(),
							itm.getIdUser()) >= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				} else {
					if (userUserSimilarityMatrix.get(activeUser.getIdUser(),
							itm.getIdUser()) <= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				}
			}
		}
		return neighborList;
	}

	
	
	public AbstractMatrix calculateItemsSimilarities() {
		AbstractMatrix tmpItemItemSimilarityMatrix = MatrixFactory.createItemsMatrix(items);
		Instances instances = MovieLens100KDataReader.fromItemsToWekaDataset(items);
		/**********weka�����������****/
		LinearNNSearch knn = new LinearNNSearch(instances);
		try {
			ManhattanDistance df = new ManhattanDistance(instances);
			//EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("3,6-last");
			//System.out.println(df.getAttributeIndices());
			knn.setDistanceFunction(df);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration itms = instances.enumerateInstances();
		while(itms.hasMoreElements()){
			Instance in = (Instance) itms.nextElement();
			 try {
				Instances nearestNeighbour = knn.kNearestNeighbours(in,instances.numInstances());
				double[] distances = knn.getDistances();
				for(int i=0;i<nearestNeighbour.numInstances();i++){
					Instance in2 = nearestNeighbour.instance(i);
					long  it1 = Long.parseLong(in.attribute(0).value((int) in.value(0)));
					long it2 = Long.parseLong(in2.attribute(0).value((int) in2.value(0)));
					//System.out.println(it1+"-"+it2);
					if ((it1 != it2) && tmpItemItemSimilarityMatrix.get(it1,it2) == 0) {
						double it1it2Sim =distances[i];
						//System.out.println(it1+"-"+it2+"="+it1it2Sim);
						tmpItemItemSimilarityMatrix.set(it1, it2,it1it2Sim);
						tmpItemItemSimilarityMatrix.set(it2, it1,it1it2Sim);
					}
				}
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tmpItemItemSimilarityMatrix;
	}

	public AbstractMatrix calculateUsersSimilarities() {
		AbstractMatrix tmpUserUserSimilarityMatrix = MatrixFactory.createUsersMatrix(users);
		Instances instances = MovieLens100KDataReader.fromUsersToWekaDataset(users);
		/**********weka�����������****/
		LinearNNSearch knn = new LinearNNSearch(instances);
		try {
			ManhattanDistance df = new ManhattanDistance(instances);
			//EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("2-last");
			//System.out.println(df.getAttributeIndices());
			knn.setDistanceFunction(df);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration usrs = instances.enumerateInstances();
		while(usrs.hasMoreElements()){
			Instance in = (Instance) usrs.nextElement();
			 try {
				Instances nearestNeighbour = knn.kNearestNeighbours(in,instances.numInstances());
				double[] distances = knn.getDistances();
				for(int i=0;i<nearestNeighbour.numInstances();i++){
					Instance in2 = nearestNeighbour.instance(i);
					long  usr1 = Long.parseLong(in.attribute(0).value((int) in.value(0)));
					long usr2 = Long.parseLong(in2.attribute(0).value((int) in2.value(0)));
					//System.out.println(it1+"-"+it2);
					if ((usr1 != usr2) && tmpUserUserSimilarityMatrix.get(usr1,usr2) == 0) {
						double usr1usr2 =distances[i];
						//System.out.println(it1+"-"+it2+"="+it1it2Sim);
						tmpUserUserSimilarityMatrix.set(usr1, usr2,usr1usr2);
						tmpUserUserSimilarityMatrix.set(usr2, usr1,usr1usr2);
					}
				}
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tmpUserUserSimilarityMatrix;
	}


	public AbstractMatrix getUserItemRatingMatrix() {
		return userItemRatingMatrix;
	}

	public void setUserItemRatingMatrix(IndexedSimpleMatrix userItemRatingMatrix) {
		this.userItemRatingMatrix = userItemRatingMatrix;
	}

	public AbstractMatrix getItemItemSimilarityMatrix() {
		return itemItemSimilarityMatrix;
	}

	public void setItemItemSimilarityMatrix(
			IndexedSimpleMatrix itemItemSimilarityMatrix) {
		this.itemItemSimilarityMatrix = itemItemSimilarityMatrix;
	}

	

	public static Class<? extends SimilarityMeasure> getPc() {
		return pc.getClass();
	}

}
