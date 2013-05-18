package com.recsys.quality;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.jcs.JCS;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.RatingItemChecker;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.cache.RecSysCache;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.ContentBasedFiltering;
import com.recsys.recommendation.Mathematics;
import com.recsys.recommendation.MyFrequentistModelHard;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;
import com.recsys.utils.PredicateUtils;

public class MyFrequentistModelML100KQualityTest {

	private static String databaseURL = "database/MovieLens";
	private static String databaseName = "ml-100K";
	private static String learningDatafile = "ua.base";
	private static String testDatafile = "ua.test";
	private static String usersDatafile = "u.user";
	private static String itemsDatafile = "u.item";
	private static String learningRatingsFile = databaseURL+"/"+databaseName+"/"+learningDatafile;
	private static String usersFile = databaseURL+"/"+databaseName+"/"+usersDatafile;
	private static String itemsFile = databaseURL+"/"+databaseName+"/"+itemsDatafile;
	private static String testRatingsFile = databaseURL+"/"+databaseName+"/"+testDatafile;
	private static final String wekaDistanceMeasure = "ManhattanDistance";//"EuclideanDistance";//


	List<Double> predictedUsersRatings = new ArrayList<Double>();
	List<Double> realUsersRatings = new ArrayList<Double>();
	
	/*
	 * private EntityManagerFactory
	 * emf=Persistence.createEntityManagerFactory("RecommenderSystem"); ItemDAO
	 * itemD=new ItemDAO(emf); UserDAO userD=new UserDAO(emf); RatingDAO
	 * ratingD=new RatingDAO(emf);
	 * 
	 * private EntityManager em=itemD.getEntityManager(); private EntityManager
	 * emu=userD.getEntityManager(); private EntityManager
	 * emr=ratingD.getEntityManager();
	 */
	private static List<User> users;
	private static List<Item> items;
	private Instances usersDataset;
	private Instances itemsDataset;
	private EM itemsClusterer;
	private EM usersClusterer;
	
	private static List<Rating> dataBaseEntries;
	private static MyFrequentistModelHard filtre;

	private static final int NC = 8;
	private static final int NG = 8;
	
	private static final String ITEMSCLUSTERDCACHE = "itemsClustered"+"_EM_"+NC;

	private static final String USERSCLUSTERDCACHE = "usersClustered"+"_EM_"+NG;



	
	
	@Before
	public void initData() throws Exception {
		
		items=(List<Item>) RecSysCache.getJcs().get(ITEMSCLUSTERDCACHE);
		if(items == null){
			items = MovieLens100KDataReader.findItemsFile(itemsFile);// itemD.findItems();
			itemsDataset =  MovieLens100KDataReader.fromItemsToWekaDataset(items);
			emItemsClustering();
			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
		}else{
			System.out.println("items clustering exists");
		}
		users=(List<User>) RecSysCache.getJcs().get(USERSCLUSTERDCACHE);
		if(users == null){
			users = MovieLens100KDataReader.findUsersFile(usersFile);// userD.findUsers();	
			usersDataset =  MovieLens100KDataReader.fromUsersToWekaDataset(users);
			emUsersClustering();
			RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
		}else{
			System.out.println("users clustering exists");
		}
		dataBaseEntries = MovieLens100KDataReader.findRatingsFile(learningRatingsFile);
		for(Rating r:dataBaseEntries){
			User u = r.getRatingUser();
			Item it = r.getRatedItem();
			int indexU = users.indexOf(u);
			int indexIt = items.indexOf(it);
			r.setRatingUser(users.get(indexU));
			r.setRatedItem(items.get(indexIt));
		}
		filtre = new MyFrequentistModelHard(users, items, dataBaseEntries);
		RecSysCache.getJcs().dispose();
	}

/*
	@Test
	public void oneUserRatingsQualityTest() {
		User activeUser = users.get(9);
		oneUserRatingsQuality(activeUser);
	}
*/
	
	// Rating estimation
	public List<RealAndPrediction> oneUserRatingsQuality(User activeUser) {
		List<Rating> allEstimations = filtre.userRatingsEstimationMaxProba(activeUser);
		/* begin Quality Test */
		List<Rating> userRealRatings = MovieLens100KDataReader.findUserRatings(
				testRatingsFile, activeUser.getIdUser());
		//System.out.println("user " + activeUser.getIdUser() + " has "	+ userRealRatings.size() + " ratings");
		if (userRealRatings.isEmpty()) {
			System.out.println("no ratings in test data so no quality measure : exit");
			return null;
		}

		List<RealAndPrediction> realsAndPredicted = new ArrayList<RealAndPrediction>();

		for (Rating r : userRealRatings) {
			double realRating = r.getRating();
			double predictedRating;

			List<Rating> estimatedItemRatings = (List<Rating>) PredicateUtils
					.findAll(allEstimations, new RatingItemChecker(r.getRatedItem()));
			if (!estimatedItemRatings.isEmpty()) {
				Rating estimatedItemRating = estimatedItemRatings.get(0);
				predictedRating = estimatedItemRating.getRating();
			} else {
				predictedRating = 0;
			}

			realsAndPredicted.add(new RealAndPrediction(realRating,
					predictedRating));
		}
		//System.out.println(realsAndPredicted);
		try {
			double mae = Mathematics.mae(realsAndPredicted);
			double rmse = Mathematics.rmse(realsAndPredicted);
			System.out.format("User %d \t %d \t %.3f \t %.3f \n", activeUser.getIdUser(), userRealRatings.size(), mae, rmse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return realsAndPredicted;
	}
/*
	@Test
	public void allUsersRatingsQualityTest() {
		List<RealAndPrediction> allPredictions = new ArrayList<RealAndPrediction>();
		for (User u : users) {
			List<RealAndPrediction> oneUserPrediction = oneUserRatingsQuality(u);
			if (oneUserPrediction != null) {
				allPredictions.addAll(oneUserPrediction);
			}
		}
		double mae = 0;
		double rmse = 0;
		try {
			mae = Mathematics.mae(allPredictions);
			rmse = Mathematics.rmse(allPredictions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("total mae = " + mae);
		System.out.println("total rmse = " + rmse);
	}

*/
	
	@Test
	public void allUsersRatingsQualityTestParallel()
			throws InterruptedException, ExecutionException {
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<Future<List<RealAndPrediction>>> futures = new ArrayList<Future<List<RealAndPrediction>>>();
		for (final User u : users) {
			Callable<List<RealAndPrediction>> callable = new Callable<List<RealAndPrediction>>() {
				public List<RealAndPrediction> call() throws Exception {
					// process your input here and compute the output
					List<RealAndPrediction> output = oneUserRatingsQuality(u);
					return output;
				}
			};
			futures.add(service.submit(callable));
		}

		service.shutdown();

		List<RealAndPrediction> outputs = new ArrayList<RealAndPrediction>();
		for (Future<List<RealAndPrediction>> future : futures) {
			if (future.get() != null) {
				outputs.addAll(future.get());
			}
		}
		double mae = 0;
		double rmse = 0;
		try {
			mae = Mathematics.mae(outputs);
			rmse = Mathematics.rmse(outputs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("total mae = " + mae);
		System.out.println("total rmse = " + rmse);
		//RecSysCache.getJcs().dispose();
	}

	

	//@Test
	public final void emItemsClustering() {
		System.out.println("testItemsClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		itemsClusterer = new EM(); // new instance of clusterer
		final String ITEMSCLUSTERDCACHE = "itemsClustered"+"_"+itemsClusterer.getClass();
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "15";
		try {
			itemsClusterer.setOptions(options); // set the options
			itemsClusterer.setNumClusters(NC);
			itemsClusterer.buildClusterer(itemsDataset);
			// System.out.println(clusterer.toString());
			eval.setClusterer(itemsClusterer); // the cluster to evaluate
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
				//System.out.println(clusterAssignments[i]);
			} // output # of clusters
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // build the clusterer
	}

	//@Test
	public final void emUsersClustering() {
		System.out.println("testUsersClustering");
		ClusterEvaluation eval = new ClusterEvaluation();
		usersClusterer = new EM(); // new instance of clusterer
		final String USERSCLUSTERDCACHE = "usersClustered"+"_"+usersClusterer.getClass();
		String[] options = new String[2];
		options[0] = "-I"; // max. iterations
		options[1] = "15";
		try {
			usersClusterer.setOptions(options); // set the options
			usersClusterer.setNumClusters(NG);
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
	}
	
//	
//	public void itemsClustering() {
//		System.out.println("testItemsClustering");
//		ClusterEvaluation eval = new ClusterEvaluation();
//		SimpleKMeans itemsClusterer = new SimpleKMeans(); // new instance of clusterer
//		itemsClusterer.setSeed(10);
//
//		final String ITEMSCLUSTERDCACHE = "itemsClustered"+"_"+itemsClusterer.getClass();
//		
//		String[] options = new String[2];
//		options[0] = "-I"; // max. iterations
//		options[1] = "20";
//		try {
//			itemsClusterer.setOptions(options); // set the options
//			itemsClusterer.setPreserveInstancesOrder(true);
//			itemsClusterer.setNumClusters(NC);
//			itemsClusterer.buildClusterer(itemsDataset);
//			// System.out.println(clusterer.toString());
//			eval.setClusterer(itemsClusterer); // the cluster to evaluate
//			eval.evaluateClusterer(itemsDataset); // data to evaluate the
//													// clusterer on
//			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
//			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
//			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
//			int[] clusterAssignments = itemsClusterer.getAssignments();
//			System.out.println(" of clustered items : "+clusterAssignments.length);
//			assertEquals(itemsDataset.numInstances(), clusterAssignments.length);
//			for(int i=0;i<items.size();i++) {
//				items.get(i).setCategory(clusterAssignments[i]);
//			} // output # of clusters
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // build the clusterer
//	}
//
//	//@Test
//	public void usersClustering() {
//		System.out.println("testUsersClustering");
//		ClusterEvaluation eval = new ClusterEvaluation();
//		SimpleKMeans usersClusterer = new SimpleKMeans(); // new instance of clusterer
//		usersClusterer.setSeed(10);
//		final String USERSCLUSTERDCACHE = "usersClustered"+"_"+usersClusterer.getClass();
//
//		String[] options = new String[2];
//		options[0] = "-I"; // max. iterations
//		options[1] = "20";
//		try {
//			usersClusterer.setOptions(options); // set the options
//			usersClusterer.setPreserveInstancesOrder(true);
//			usersClusterer.setNumClusters(NG);
//			usersClusterer.buildClusterer(usersDataset);
//			// System.out.println(clusterer.toString());
//			eval.setClusterer(usersClusterer); // the cluster to evaluate
//			eval.evaluateClusterer(usersDataset); // data to evaluate the
//													// clusterer on
//			System.out.println("# of clusters: " + eval.getNumClusters()); // output # of clusters
//			System.out.println("LogLikelihood: " + eval.getLogLikelihood()); // output # of clusters
//			System.out.println("ClassesToClusters(): " + eval.getClassesToClusters()); // output # of clusters
//			int[] clusterAssignments = usersClusterer.getAssignments();
//			System.out.println(" of clustered items : "+clusterAssignments.length);
//			assertEquals(usersDataset.numInstances(), clusterAssignments.length);
//			for(int i=0;i<users.size();i++) {
//				users.get(i).setGroup(clusterAssignments[i]);
//			} // output # of clusters
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // build the clusterer
//	}
}
