package com.recsys.custering;

import java.util.List;

import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;

public class UsersDemographicsKmeansClusterer implements UsersClusterer {
	private final int NG = 40;
	@Override
	public List<User> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances usersDataset  =  MovieLens100KDataReader.fromUsersToWekaDataset(users);

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
			df.setAttributeIndices("2-4");
			usersClusterer.setDistanceFunction(df);
			usersClusterer.buildClusterer(usersDataset);
			// System.out.println(clusterer.toString());
			int[] clusterAssignments = usersClusterer.getAssignments();
			for (int i = 0; i < users.size(); i++) {
				users.get(i).setGroup(clusterAssignments[i]);
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return users;

	}

	@Override
	public String toString() {
		return "UsersDemographicsKmeansClusterer_NG_"+NG;
	}

	

}
