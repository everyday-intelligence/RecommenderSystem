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
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UsersNORMRatingsKmeansClusterer implements UsersClusterer {
	private final int NG = 20;
	
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
			for (int i = 0; i < users.size(); i++) {
				users.get(i).setGroup(clusterAssignments[i]);
				users.get(i).setGroupsMemberships(usersClusterer.distributionForInstance(usersDataset.instance(i)));

			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
		return users;

	}

	private Instances createUsersRatingsDataset(List<Item> items,List<User> users, List<Rating> ratings){
		AbstractMatrix userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
		userItemRatingMatrix.NonBiasedRow();
		List<Instance> instancesList = new ArrayList<Instance>();
		for(int i=0;i<users.size();i++){
			double[] attVals = new double[items.size()]; 
			List<Double> userRatings = userItemRatingMatrix.getRow(i).toList();
			for(int j=0;j<items.size();j++){
				attVals[j]=userRatings.get(j);
			}
			instancesList.add(new Instance(1,attVals));
		}
		
		FastVector attributes = new FastVector();
		for(Item it:items){
			attributes.addElement(new Attribute(it.getIdItem()+""));
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
		return "UsersNORMRatingsKmeansClusterer_NG_"+NG;
	}

	@Override
	public int getNG() {
		// TODO Auto-generated method stub
		return NG;
	}

}
