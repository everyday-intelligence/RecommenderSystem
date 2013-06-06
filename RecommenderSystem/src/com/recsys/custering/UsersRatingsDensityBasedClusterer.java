package com.recsys.custering;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
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
import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UsersRatingsDensityBasedClusterer implements UsersClusterer {
	//private final int NG = 30;
	
	private int NG = 30;

	@Override
	public List<User> cluster(List<Item> items,List<User> users, List<Rating> ratings) {
		Instances usersDataset  =  createUsersRatingsDataset(items, users, ratings);
		MakeDensityBasedClusterer usersClusterer = new MakeDensityBasedClusterer(); // new instance of clusterer
		ClusterEvaluation eval = new ClusterEvaluation();

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
				NG = eval.getNumClusters();
				System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
				System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
				System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
				double[] clusterAssignments = eval.getClusterAssignments();
				System.out.println(" of clustered items : "+clusterAssignments.length);
				assertEquals(usersDataset.numInstances(), clusterAssignments.length);
				for(int i=0;i<users.size();i++) {
					users.get(i).setGroup(clusterAssignments[i]);
					users.get(i).setGroupsMemberships(usersClusterer.distributionForInstance(usersDataset.instance(i)));

					//System.out.println(clusterAssignments[i]);
				} // output # of clusters
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // build the clusterer
			return users;

		}


	private Instances createUsersRatingsDataset(List<Item> items,List<User> users, List<Rating> ratings){
		IndexedSimpleMatrix userItemRatingMatrix = MatrixFactory.createMatrix(users, items);
		for (Rating r : ratings) {
			userItemRatingMatrix.set(r.getRatingUser().getIdUser(),
					r.getRatedItem().getIdItem(),
					r.getRating());
		}
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
		return "UsersRatingsMakeDensityBasedClusterer_NG_";//+NG;
	}

	@Override
	public int getNG() {
		// TODO Auto-generated method stub
		return NG;
	}

}
