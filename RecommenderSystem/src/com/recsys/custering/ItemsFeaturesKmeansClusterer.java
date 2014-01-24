package com.recsys.custering;

import java.util.List;

import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;

public class ItemsFeaturesKmeansClusterer implements ItemsClusterer {
	private final int NC =55;

	@Override
	public List<Item> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances itemsDataset = MovieLens100KDataReader.fromItemsToWekaDataset(items);
		SimpleKMeans itemsClusterer = new SimpleKMeans(); // new instance of
															// clusterer
		itemsClusterer.setSeed(10);

		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			itemsClusterer.setOptions(options); // set the options
			itemsClusterer.setPreserveInstancesOrder(true);
			itemsClusterer.setNumClusters(NC+1);//il soustrait un je ne sais pas pk
			EuclideanDistance df = new EuclideanDistance(itemsDataset);
			// EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("3,6-last");
			itemsClusterer.setDistanceFunction(df);
			itemsClusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			int[] clusterAssignments = itemsClusterer.getAssignments();
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setCategory(clusterAssignments[i]);
				items.get(i).setCategoriesMemberships(itemsClusterer.distributionForInstance(itemsDataset.instance(i)));
				//if(itemsClusterer.distributionForInstance(itemsDataset.instance(i)).length!=this.NC){
					//System.out.println("erreurrrrrr  NC="+this.NC+" algo="+itemsClusterer.distributionForInstance(itemsDataset.instance(i)).length);
				//}
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return items;
	}
	@Override
	public String toString() {
		return "ItemsFeaturesKmeansClusterer_NC_"+NC;
	}
	@Override
	public int getNC() {
		return NC;
	}

}
