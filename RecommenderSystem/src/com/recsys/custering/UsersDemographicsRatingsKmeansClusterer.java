package com.recsys.custering;

import java.util.ArrayList;
import java.util.List;

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
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UsersDemographicsRatingsKmeansClusterer implements UsersClusterer {
	private final int NG = 3;
	
	@Override
	public List<User> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances usersDataset  =  createUsersRatingsDataset(items, users, ratings);
		SimpleKMeans usersClusterer = new SimpleKMeans(); // new instance of
															// clusterer
		usersClusterer.setSeed(10);

		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			usersClusterer.setOptions(options); // set the options
			usersClusterer.setPreserveInstancesOrder(true);
			usersClusterer.setNumClusters(NG);
			EuclideanDistance df = new EuclideanDistance(usersDataset);
			// EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			//All attributs df.setAttributeIndices("2-4");
			usersClusterer.setDistanceFunction(df);
			usersClusterer.buildClusterer(usersDataset);
			// System.out.println(clusterer.toString());
			int[] clusterAssignments = usersClusterer.getAssignments();
			for (int i = 0; i < users.size(); i++){
				users.get(i).setGroup(clusterAssignments[i]);
				users.get(i).setGroupsMemberships(usersClusterer.distributionForInstance(usersDataset.instance(i)));
				//System.out.println(users.get(i).getGroupsMemberships());
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return users;

	}

	private Instances createUsersRatingsDataset(List<Item> items,List<User> users, List<Rating> ratings){
		Instances usersDemographicsDataset  =  MovieLens100KDataReader.fromUsersToWekaDataset(users);
		Instances usersDemographicsRatingsDataset = new Instances(usersDemographicsDataset);
		
		IndexedSimpleMatrix userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
		
		for(Item it : items){
			usersDemographicsRatingsDataset.insertAttributeAt(new Attribute(it.getIdItem()+""), usersDemographicsRatingsDataset.numAttributes());
			for(int i=0;i<users.size();i++){
				usersDemographicsRatingsDataset.instance(i).setValue(usersDemographicsRatingsDataset.numAttributes() - 1, userItemRatingMatrix.get(users.get(i).getIdUser(), it.getIdItem()));
			}
		}
		
		
		return usersDemographicsRatingsDataset;
	}
	
	@Override
	public String toString() {
		return "UsersDemographicsRatingsKmeansClusterer_NG_"+NG;
	}

	public int getNG() {
		return NG;
	}

	

}
