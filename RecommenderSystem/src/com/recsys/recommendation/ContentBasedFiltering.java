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

public class ContentBasedFiltering implements
		RecommendationStrategy {

	private List<Item> items;
	private AbstractMatrix userItemRatingMatrix;
	private AbstractMatrix itemItemSimilarityMatrix;

	public static final int K = 100;
	public static SimilarityMeasure<Double> pc = new ManhattanDistanceNumber<Double>();
	public static CF_IC_RatingAggregator na = new MeanAggregator();

	
	public ContentBasedFiltering(List<Item> items, AbstractMatrix userItemRatingMatrix, AbstractMatrix itemItemSimilarityMatrix) {
		super();
		this.items = items;
		this.userItemRatingMatrix = userItemRatingMatrix;
		if(itemItemSimilarityMatrix == null){
			itemItemSimilarityMatrix = calculateItemsSimilarities();			
			System.out.println("finished calculating item item similarities 2");
		}
		this.itemItemSimilarityMatrix = itemItemSimilarityMatrix;			
	}
	
	public ContentBasedFiltering(List<User> users,
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
	}

	@Override
	public List<Recommendation> recommend(User activeUser) {
		List<Rating> allPossibleCandidatesEstimation = userRatingsEstimation(activeUser);
		if (allPossibleCandidatesEstimation == null
				|| allPossibleCandidatesEstimation.isEmpty()) {
			List<Recommendation> allPossibleCandidates = new ArrayList<Recommendation>(allPossibleCandidatesEstimation.size());
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
		List<Rating> allRatingsEstimations = new ArrayList<Rating>(items.size());
		for (Item it : items) {
			ArrayList<Item> itemNeighborhoodRatedByUser = itemNeighborhoodRatedByUser(activeUser, it);
			Double estimatedRating = na.aggregate(activeUser, it, itemItemSimilarityMatrix, userItemRatingMatrix, itemNeighborhoodRatedByUser );
			allRatingsEstimations.add(new Rating(estimatedRating, it, activeUser));
		}

		return allRatingsEstimations;
	}
	
	
	public ArrayList<Item> itemNeighborhoodRatedByUser(User user, Item item) {
		ArrayList<Item> itemsRatedByUser = new ArrayList<Item>();
		List<Double> itemRatedSimilarityValues = new ArrayList<Double>();

		for(Item it:items){
			if(userItemRatingMatrix.get(user.getIdUser(), it.getIdItem())!=0){
				itemsRatedByUser.add(it);
				itemRatedSimilarityValues.add(itemItemSimilarityMatrix.get(item.getIdItem(), it.getIdItem()));
			}
		}
		
		Collections.sort(itemRatedSimilarityValues);
		if (pc.isSimilarity()) {
			Collections.reverse(itemRatedSimilarityValues);
		}
		int nbS = Math.min(
				itemRatedSimilarityValues.size() - 1, K);
		double threashold = itemRatedSimilarityValues.get(nbS);
		ArrayList<Item> neighborList = new ArrayList<Item>(nbS);

		for (Item itm : itemsRatedByUser) {
			if (itemItemSimilarityMatrix.get(item.getIdItem(), itm.getIdItem()) != Double.NaN) {
				if (pc.isSimilarity()) {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) >= threashold) {
						neighborList.add(itm);
						// System.out.println(simMap.get(u)+" taken");
					}
				} else {
					if (itemItemSimilarityMatrix.get(item.getIdItem(),
							itm.getIdItem()) <= threashold) {
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


	public AbstractMatrix getUserItemRatingMatrix() {
		return userItemRatingMatrix;
	}

	public void setUserItemRatingMatrix(AbstractMatrix userItemRatingMatrix) {
		this.userItemRatingMatrix = userItemRatingMatrix;
	}

	public AbstractMatrix getItemItemSimilarityMatrix() {
		return itemItemSimilarityMatrix;
	}

	public void setItemItemSimilarityMatrix(
			IndexedSimpleMatrix itemItemSimilarityMatrix) {
		this.itemItemSimilarityMatrix = itemItemSimilarityMatrix;
	}

	public static int getK() {
		return K;
	}

	public static Class<? extends SimilarityMeasure> getPc() {
		return pc.getClass();
	}

}
