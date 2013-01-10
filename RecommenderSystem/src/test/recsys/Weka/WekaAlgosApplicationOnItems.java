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

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;

public class WekaAlgosApplicationOnItems {
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
		System.out.println(itemsDataset.toSummaryString());
		//System.out.println(itemsDataset);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDuplicate() {
		System.out.println(itemsDataset);
		System.out.println(itemsDataset.instance(0).value(3));
	}

	@Test
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
			clusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(clusterer); // the cluster to evaluate
			eval.evaluateClusterer(itemsDataset); // data to evaluate the
													// clusterer on
			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
			double[] clusterAssignments = eval.getClusterAssignments();
			System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(itemsDataset.numInstances(), clusterAssignments.length);
			for(int i=0;i<items.size();i++) {
				items.get(i).setCategory(clusterAssignments[i]);
			} // output # of clusters
			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
			RecSysCache.getJcs().dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}

	@Test
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
			double[] clusterAssignments = eval.getClusterAssignments();
			System.out.println(" of clustered items : "+clusterAssignments.length);
			assertEquals(usersDataset.numInstances(), clusterAssignments.length);
			for(int i=0;i<users.size();i++) {
				users.get(i).setGroup(clusterAssignments[i]);
			} // output # of clusters
			RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
			RecSysCache.getJcs().dispose();
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

}
