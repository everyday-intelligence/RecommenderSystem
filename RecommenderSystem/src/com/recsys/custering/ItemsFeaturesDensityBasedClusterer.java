package com.recsys.custering;

import static org.junit.Assert.assertEquals;

import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;

public class ItemsFeaturesDensityBasedClusterer implements ItemsClusterer {

	private int NC = 10;
	@Override
	public List<Item> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances itemsDataset = MovieLens100KDataReader
				.fromItemsToWekaDataset(items);
		System.out.println("testItemsClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		MakeDensityBasedClusterer itemsClusterer = new MakeDensityBasedClusterer(); // new instance of clusterer
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "15";
		try {
			//itemsClusterer.setOptions(options); // set the options
			itemsClusterer.setNumClusters(NC);
			itemsClusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(itemsClusterer); // the cluster to evaluate
			eval.evaluateClusterer(itemsDataset); // data to evaluate the
													// clusterer on
			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			NC = eval.getNumClusters();
			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
			double[] clusterAssignments = eval.getClusterAssignments();
			System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(itemsDataset.numInstances(), clusterAssignments.length);
			for(int i=0;i<items.size();i++) {
				items.get(i).setCategory(clusterAssignments[i]);
				items.get(i).setCategoriesMemberships(itemsClusterer.distributionForInstance(itemsDataset.instance(i)));
				//System.out.println(clusterAssignments[i]);
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return items;
	}
	@Override
	public String toString() {
		return "ItemsMakeDensityBasedClusterer";
	}
	@Override
	public int getNC() {
		return NC;
	}

}
