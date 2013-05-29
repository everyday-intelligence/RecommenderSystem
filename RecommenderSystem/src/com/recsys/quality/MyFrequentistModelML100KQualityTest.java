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
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.RatingItemChecker;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.cache.RecSysCache;
import com.recsys.custering.ItemsClusterer;
import com.recsys.custering.ItemsFeaturesKmeansClusterer;
import com.recsys.custering.UsersClusterer;
import com.recsys.custering.UsersDemographicsKmeansClusterer;
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
	
	private static List<Rating> dataBaseEntries;
	private static MyFrequentistModelHard filtre;

	private ItemsClusterer itemsClusterer = new ItemsFeaturesKmeansClusterer();
	private UsersClusterer usersClusterer = new UsersDemographicsKmeansClusterer();
	
	private final String ITEMSCLUSTERDCACHE = "itemsClustered"+itemsClusterer.toString();
	private final String USERSCLUSTERDCACHE = "usersClustered"+usersClusterer.toString();

	@Before
	public void initData() throws Exception {
		dataBaseEntries = MovieLens100KDataReader.findRatingsFile(learningRatingsFile);
		items=(List<Item>) RecSysCache.getJcs().get(ITEMSCLUSTERDCACHE);
		if(items == null){
			items = MovieLens100KDataReader.findItemsFile(itemsFile);// itemD.findItems();
			items = itemsClusterer.cluster(items, users, dataBaseEntries);
			//emItemsClustering();
			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
		}else{
			System.out.println("items clustering exists");
		}
		users=(List<User>) RecSysCache.getJcs().get(USERSCLUSTERDCACHE);
		if(users == null){
			users = MovieLens100KDataReader.findUsersFile(usersFile);// userD.findUsers();	
			users = usersClusterer.cluster(items, users, dataBaseEntries);
			//emUsersClustering();
			RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
		}else{
			System.out.println("users clustering exists");
		}
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
		List<Rating> allEstimations = filtre.userRatingsEstimationExpectation(activeUser);
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
}
