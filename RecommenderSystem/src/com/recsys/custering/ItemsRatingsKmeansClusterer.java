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

public class ItemsRatingsKmeansClusterer implements ItemsClusterer {
	private final int NC = 40;
	
	
	@Override
	public List<Item> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances itemsDataset  =  createItemsRatingsDataset(items, users, ratings);
		SimpleKMeans itemsClusterer = new SimpleKMeans(); // new instance of
															// clusterer
		itemsClusterer.setSeed(10);

		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			itemsClusterer.setOptions(options); // set the options
			itemsClusterer.setPreserveInstancesOrder(true);
			itemsClusterer.setNumClusters(NC);
			EuclideanDistance df = new EuclideanDistance(itemsDataset);
			// EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			//All attributs df.setAttributeIndices("2-4");
			itemsClusterer.setDistanceFunction(df);
			itemsClusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			int[] clusterAssignments = itemsClusterer.getAssignments();
			for (int i = 0; i < items.size(); i++) {
				items.get(i).setCategory(clusterAssignments[i]);
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return items;
	}

	private Instances createItemsRatingsDataset(List<Item> items,List<User> users, List<Rating> ratings){
		IndexedSimpleMatrix userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
		List<Instance> instancesList = new ArrayList<Instance>();
		for(int i=0;i<items.size();i++){
			double[] attVals = new double[users.size()]; 
			List<Double> itemRatings = userItemRatingMatrix.getColumn(i).toList();
			for(int j=0;j<items.size();j++){
				attVals[j]=itemRatings.get(j);
			}
			instancesList.add(new Instance(1,attVals));
		}
		
		FastVector attributes = new FastVector();
		for(User us:users){
			attributes.addElement(new Attribute(us.getIdUser()+""));
		}
		Instances data = new Instances("MyData", attributes, 0);
		for(Instance in:instancesList){
			in.setDataset(data);
			data.add(in);
		}
		return data;
	}
	
	@Override
	public String toString() {
		return "ItemsRatingsKmeansClusterer_NC_"+NC;
	}

	

}
