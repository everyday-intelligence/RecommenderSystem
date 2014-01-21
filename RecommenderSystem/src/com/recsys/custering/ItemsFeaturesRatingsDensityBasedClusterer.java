package com.recsys.custering;

import java.util.ArrayList;
import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.DomainDAO.MovieLensItemDataParser;
import com.recsys.DomainDAO.MovieLensUserDataParser;
import com.recsys.cache.RecSysCache;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class ItemsFeaturesRatingsDensityBasedClusterer implements ItemsClusterer {
	private int NC = 30;
	
	
	@Override
	public List<Item> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances itemsDataset  =  createItemsRatingsDataset(items, users, ratings);
		MakeDensityBasedClusterer itemsClusterer = new MakeDensityBasedClusterer(); // new instance of clusterer
		ClusterEvaluation eval = new ClusterEvaluation();

		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "1";
		try {
			//itemsClusterer.setOptions(options); // set the options
			//itemsClusterer.setPreserveInstancesOrder(true);
			itemsClusterer.setNumClusters(NC);
			//All attributs df.setAttributeIndices("2-4");
			itemsClusterer.buildClusterer(itemsDataset);
			eval.setClusterer(itemsClusterer); // the cluster to evaluate
			eval.evaluateClusterer(itemsDataset); // data to evaluate the
			// System.out.println(clusterer.toString());
			double[] clusterAssignments = eval.getClusterAssignments();
			//NC = eval.getNumClusters();
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setCategory(clusterAssignments[i]);
				items.get(i).setCategoriesMemberships(itemsClusterer.distributionForInstance(itemsDataset.instance(i)));
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return items;
	}


	private Instances createItemsRatingsDataset(List<Item> items,List<User> users, List<Rating> ratings){
		Instances itemsFeaturesDataset  =  MovieLens100KDataReader.fromItemsToWekaDataset(items);
		Instances itemsFeaturesRatingsDataset = new Instances(itemsFeaturesDataset);
		
		AbstractMatrix userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
		
		for(User u : users){
			itemsFeaturesRatingsDataset.insertAttributeAt(new Attribute(u.getIdUser()+""), itemsFeaturesRatingsDataset.numAttributes());
			for(int i=0;i<items.size();i++){
				itemsFeaturesRatingsDataset.instance(i).setValue(itemsFeaturesRatingsDataset.numAttributes() - 1, userItemRatingMatrix.get(u.getIdUser(), items.get(i).getIdItem()));
			}
		}
		return itemsFeaturesRatingsDataset;
	}
	
	@Override
	public String toString() {
		return "ItemsFeaturesRatingsMakeDensityBasedClusterer_NC_"+NC;
	}

	public int getNC() {
		return NC;
	}

	

}
