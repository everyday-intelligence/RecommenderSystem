package test.recsys.Weka;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.cache.RecSysCache;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.matrix.MatrixFactory;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;

public class WekaAlgosApplication {
	private static String learningRatingsFile = "database/MovieLens/ml-100K/ua.base";
	private static String usersFile = "database/MovieLens/ml-100K/u.user";
	private static String itemsFile = "database/MovieLens/ml-100K/u.item";
	private static String testRatingsFile = "database/MovieLens/ml-100K/ua.test";

	private List<Item> items;
	private Instances itemsDataset;
	private List<User> users;
	private Instances usersDataset;

	@Before
	public void setUp() throws Exception {
		if (itemsDataset == null) {
			items = MovieLens100KDataReader.findItemsFile(itemsFile);
			itemsDataset = MovieLens100KDataReader.fromItemsToWekaDataset(items);
			users = MovieLens100KDataReader.findUsersFile(usersFile);
			usersDataset = MovieLens100KDataReader.fromUsersToWekaDataset(users);
		}
		//System.out.println(itemsDataset.toSummaryString());
		//System.out.println(itemsDataset);
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public final void testDuplicate() {
		System.out.println(itemsDataset);
		System.out.println(itemsDataset.instance(0).value(3));
	}

	
	//@Test
	public final void kmeansItemsClustering() {
		//System.out.println(itemsDataset.toSummaryString());
		System.out.println("testItemsClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		SimpleKMeans clusterer = new SimpleKMeans(); // new instance of clusterer
		clusterer.setSeed(10);

		final String ITEMSCLUSTERDCACHE = "itemsClustered"+"_"+clusterer.getClass();
		
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			clusterer.setOptions(options); // set the options
			clusterer.setPreserveInstancesOrder(true);
			clusterer.setNumClusters(20);
			EuclideanDistance df = new EuclideanDistance(itemsDataset);
			//EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("3,6-last");
			clusterer.setDistanceFunction(df);
			clusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(clusterer); // the cluster to evaluate
			eval.evaluateClusterer(itemsDataset); // data to evaluate the
													// clusterer on
			//System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			System.out.println("clusterResultsToString: " + eval.clusterResultsToString()); // output # of clusters
			//int[] clusterAssignments = clusterer.getAssignments();
			//System.out.println(" of clustered items : "+clusterAssignments.length);
			//assertEquals(itemsDataset.numInstances(), clusterAssignments.length);
//			for(int i=0;i<items.size();i++) {
//				items.get(i).setCategory(clusterAssignments[i]);
//			} // output # of clusters
//			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
//			RecSysCache.getJcs().dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}

	@Test
	public final void kmeansUsersClustering() {
		System.out.println("testUsersClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		SimpleKMeans clusterer = new SimpleKMeans(); // new instance of clusterer
		clusterer.setSeed(10);
		final String USERSCLUSTERDCACHE = "usersClustered"+"_"+clusterer.getClass();

		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			clusterer.setOptions(options); // set the options
			clusterer.setPreserveInstancesOrder(true);
			clusterer.setNumClusters(40);
			EuclideanDistance df = new EuclideanDistance(itemsDataset);
			//EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("2-4");
			clusterer.setDistanceFunction(df);
			clusterer.buildClusterer(usersDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(clusterer); // the cluster to evaluate
			eval.evaluateClusterer(usersDataset); // data to evaluate the
			System.out.println("clusterResultsToString: " + eval.clusterResultsToString()); // output # of clusters
//			
//			int[] clusterAssignments = clusterer.getAssignments();
//			System.out.println(" of clustered items : "+clusterAssignments.length);
//			assertEquals(usersDataset.numInstances(), clusterAssignments.length);
//			for(int i=0;i<users.size();i++) {
//				users.get(i).setGroup(clusterAssignments[i]);
//			} // output # of clusters
//			RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
//			RecSysCache.getJcs().dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}
	
	
	
	
	//@Test
	public final void testItemsClustering() {
		System.out.println("testItemsClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		EM clusterer = new EM(); // new instance of clusterer
		final String ITEMSCLUSTERDCACHE = "itemsClustered"+"_"+clusterer.getClass();
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			clusterer.setOptions(options); // set the options
			//clusterer.setNumClusters(50);
			clusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(clusterer); // the cluster to evaluate
			eval.evaluateClusterer(itemsDataset); // data to evaluate the
													// clusterer on
			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
			System.out.println("getClusterPriors: "+clusterer.getClusterPriors());
			double[] clusterAssignments = eval.getClusterAssignments();
			System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(itemsDataset.numInstances(), clusterAssignments.length);
//			for(int i=0;i<items.size();i++) {
//				System.out.println("cluster distribution");
//				double[] cd = clusterer.distributionForInstance(itemsDataset.instance(i));
////				for(int j=0;j<cd.length;j++){
////					System.out.print("item "+i+" : ");
////					System.out.print("class "+j+" : "+cd[j]+" | ");
////				}
////				System.out.println();
//				items.get(i).setCategory(clusterAssignments[i]);
//			} // output # of clusters
////			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
////			RecSysCache.getJcs().dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}

	//@Test
	public final void testUsersClustering() {
		System.out.println("testUsersClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		EM clusterer = new EM(); // new instance of clusterer
		final String USERSCLUSTERDCACHE = "usersClustered"+"_"+clusterer.getClass();
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "20";
		try {
			clusterer.setOptions(options); // set the options
			clusterer.buildClusterer(usersDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(clusterer); // the cluster to evaluate
			eval.evaluateClusterer(usersDataset); // data to evaluate the
													// clusterer on
			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
			System.out.println("getClusterPriors: "+clusterer.getClusterPriors());
			double[] clusterAssignments = eval.getClusterAssignments();
			//System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(usersDataset.numInstances(), clusterAssignments.length);
			//for(int i=0;i<users.size();i++) {
				//users.get(i).setGroup(clusterAssignments[i]);
			//} // output # of clusters
			//RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
			//RecSysCache.getJcs().dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}
	// @Test
	public final void testKNN() {
		LinearNNSearch knn = new LinearNNSearch(itemsDataset);
		try {
			ManhattanDistance df = new ManhattanDistance(itemsDataset);
			// EuclideanDistance df = new EuclideanDistance(itemsDataset);
			df.setDontNormalize(false);
			df.setAttributeIndices("3,6-last");
			// System.out.println(df.getAttributeIndices());
			knn.setDistanceFunction(df);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration itms = itemsDataset.enumerateInstances();
		while (itms.hasMoreElements()) {
			Instance in = (Instance) itms.nextElement();
			try {
				Instances nearestNeighbour = knn.kNearestNeighbours(in,
						itemsDataset.numInstances());
				double[] distances = knn.getDistances();
				for (int i = 0; i < nearestNeighbour.numInstances(); i++) {
					Instance in2 = nearestNeighbour.instance(i);
					// System.out.println(distances.length);
					System.out.println(in.attribute(0).value((int) in.value(0))
							+ " _ "
							+ (in2.attribute(0).value((int) in2.value(0)))
							+ " = " + distances[i]);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	//@Test
	public void calculateUsersSimilarities() {
		AbstractMatrix tmpUserUserSimilarityMatrix = MatrixFactory.createUsersMatrix(users);
		System.out.println(usersDataset);
		/**********weka�����������****/
		LinearNNSearch knn = new LinearNNSearch(usersDataset);
		try {
			ManhattanDistance df = new ManhattanDistance(usersDataset);
			//EuclideanDistance df = new EuclideanDistance(instances);
			df.setDontNormalize(false);
			df.setAttributeIndices("2-last");
			System.out.println(df.getAttributeIndices());
			knn.setDistanceFunction(df);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration usrs = usersDataset.enumerateInstances();
		while(usrs.hasMoreElements()){
			Instance in = (Instance) usrs.nextElement();
			 try {
				Instances nearestNeighbour = knn.kNearestNeighbours(in,usersDataset.numInstances());
				double[] distances = knn.getDistances();
				for(int i=0;i<nearestNeighbour.numInstances();i++){
					Instance in2 = nearestNeighbour.instance(i);
					long  usr1 = Long.parseLong(in.attribute(0).value((int) in.value(0)));
					long usr2 = Long.parseLong(in2.attribute(0).value((int) in2.value(0)));
					//System.out.println(it1+"-"+it2);
					if ((usr1 != usr2) && tmpUserUserSimilarityMatrix.get(usr1,usr2) == 0) {
						double usr1usr2 =distances[i];
						System.out.println(usr1+"-"+usr2+"="+usr1usr2);
						tmpUserUserSimilarityMatrix.set(usr1, usr2,usr1usr2);
						tmpUserUserSimilarityMatrix.set(usr2, usr1,usr1usr2);
					}
				}
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
