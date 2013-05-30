package com.recsys.custering;

import static org.junit.Assert.assertEquals;

import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;

public class UsersDemographicsEMClusterer implements UsersClusterer {

	@Override
	public List<User> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances usersDataset  =  MovieLens100KDataReader.fromUsersToWekaDataset(users);
		ClusterEvaluation eval = new ClusterEvaluation();
		EM usersClusterer = new EM(); // new instance of clusterer
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "15";
		try {
			usersClusterer.setOptions(options); // set the options
			//usersClusterer.setNumClusters(NG);
			usersClusterer.buildClusterer(usersDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(usersClusterer); // the cluster to evaluate
			eval.evaluateClusterer(usersDataset); // data to evaluate the
													// clusterer on
			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
			double[] clusterAssignments = eval.getClusterAssignments();
			System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(usersDataset.numInstances(), clusterAssignments.length);
			for(int i=0;i<users.size();i++) {
				users.get(i).setGroup(clusterAssignments[i]);
				//System.out.println(clusterAssignments[i]);
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return users;

	}

	@Override
	public String toString() {
		return "UsersEMClusterer";
	}

	

}
