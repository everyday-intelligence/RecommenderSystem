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
import com.recsys.custering.*;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.IndexedSimpleMatrix;
import com.recsys.recommendation.ContentBasedFiltering;
import com.recsys.recommendation.Mathematics;
import com.recsys.recommendation.MyFrequentistModelHard;
import com.recsys.recommendation.MyFrequentistModelSoft;
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

	private ItemsClusterer itemsClusterer = new ItemsRatingsKmeansClusterer();
	private UsersClusterer usersClusterer = new UsersDemographicsNORMRatingsKmeansClusterer();
	
	private String ITEMSCLUSTERDCACHE = "";
	private String USERSCLUSTERDCACHE = "";
 
	private static final String userItemRatingMatrixCacheID = "userItemRatingMatrix"+"_"+databaseName+"_"+learningDatafile;

	@Before
	public void initData() throws Exception {
		
		dataBaseEntries = MovieLens100KDataReader.findRatingsFile(learningRatingsFile);
		USERSCLUSTERDCACHE+=usersClusterer.toString();
		ITEMSCLUSTERDCACHE+=itemsClusterer.toString();
		items=(List<Item>) RecSysCache.getJcs().get(ITEMSCLUSTERDCACHE);
		if(items == null){
			System.out.println("miss "+ITEMSCLUSTERDCACHE);
			items = MovieLens100KDataReader.findItemsFile(itemsFile);// itemD.findItems();
			users = MovieLens100KDataReader.findUsersFile(usersFile);// userD.findUsers();
			items = itemsClusterer.cluster(items, users, dataBaseEntries);
			RecSysCache.getJcs().put(ITEMSCLUSTERDCACHE, items);
		}else{
			System.out.println("hit "+ITEMSCLUSTERDCACHE);
		}
		users=(List<User>) RecSysCache.getJcs().get(USERSCLUSTERDCACHE);
		if(users == null){
			users = MovieLens100KDataReader.findUsersFile(usersFile);// userD.findUsers();
			users = usersClusterer.cluster(items, users, dataBaseEntries);
			RecSysCache.getJcs().put(USERSCLUSTERDCACHE, users);
		}else{
			System.out.println("hit "+USERSCLUSTERDCACHE);			
		}

		

		for(Rating r:dataBaseEntries){
			User u = r.getRatingUser();
			Item it = r.getRatedItem();
			int indexU = users.indexOf(u);
			int indexIt = items.indexOf(it);
			r.setRatingUser(users.get(indexU));
			r.setRatedItem(items.get(indexIt));
		}
		RecSysCache.getJcs().dispose();
		System.out.println("NG = "+usersClusterer.getNG());
		System.out.println("NC = "+itemsClusterer.getNC());
		//filtre = new MyFrequentistModelSoft(users, items, dataBaseEntries,usersClusterer.getNG(),itemsClusterer.getNC());
		AbstractMatrix userItemRatingMatrix = (AbstractMatrix) RecSysCache.getJcs().get(userItemRatingMatrixCacheID);
		if(userItemRatingMatrix == null){
			filtre = new MyFrequentistModelHard(users, items, dataBaseEntries);
		}else{
			filtre = new MyFrequentistModelHard(users, items, dataBaseEntries,userItemRatingMatrix);			
		}
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

		List<RealAndPrediction> realsAndPredicted = new ArrayList<RealAndPrediction>(userRealRatings.size());

		for (Rating r : userRealRatings) {
			double realRating = r.getRating();
			double predictedRating;

			List<Rating> estimatedItemRatings = (List<Rating>) PredicateUtils
					.findAll(allEstimations, new RatingItemChecker(r.getRatedItem()));
			if (!estimatedItemRatings.isEmpty()) {
				Rating estimatedItemRating = estimatedItemRatings.get(0);
				predictedRating = estimatedItemRating.getRating();
				if(Double.isNaN(predictedRating) || Double.isInfinite(predictedRating)){
					predictedRating = 0;
				}
			} else {
				predictedRating = 0;
			}

			realsAndPredicted.add(new RealAndPrediction(realRating,predictedRating));
		}
		//System.out.println(realsAndPredicted);
		try {
			double mae = Mathematics.mae(realsAndPredicted);
			double rmse = Mathematics.rmse(realsAndPredicted);
			System.out.format("User %d \t %d \t %.3f \t %.3f \n", activeUser.getIdUser(), userRealRatings.size(), mae, rmse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return realsAndPredicted;
	}

	//@Test
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
			e.printStackTrace();
		}
		System.out.println("total mae = " + mae);
		System.out.println("total rmse = " + rmse);
	}



	
	@Test
	public void allUsersRatingsQualityTestParallel()
			throws InterruptedException, ExecutionException {
		int threads =  Runtime.getRuntime().availableProcessors() ;
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
			e.printStackTrace();
		}
		System.out.println("total mae = " + mae);
		System.out.println("total rmse = " + rmse);
		//RecSysCache.getJcs().dispose();
	}
	//@Test
	public void testUsersMemberships(){
		for(User u:users){
			//System.out.println(u.getGroupsMemberships().length);
//			double[] gm = u.getGroupsMemberships();
//			double sum = 0d;
//			for(int i=0;i<gm.length;i++){
//				sum+=gm[i];
//			}
//			System.out.println(sum);
		}
	}
	//@Test
	public void testItemsMemberships(){
		for(Item u:items){
			System.out.println(u.getCategoriesMemberships().length);
//			double[] gm = u.getCategoriesMemberships();
//			double sum = 0d;
//			for(int i=0;i<gm.length;i++){
//				sum+=gm[i];
//				System.out.print(gm[i]+" ");
//			}
//			System.out.println("");
//			System.out.println(sum);
		}
	}
}
