package com.recsys.quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.RatingItemChecker;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens1MDataReader;
import com.recsys.recommendation.Mathematics;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;
import com.recsys.utils.PredicateUtils;

public class UserCenteredCollaborativeFilteringML1MQualityTest {

	private static String learningRatingsFile = "database/MovieLens/ml-1m/ra.train";
	private static String usersFile = "database/MovieLens/ml-1m/users.dat";
	private static String itemsFile = "database/MovieLens/ml-1m/movies.dat";
	private static String testRatingsFile = "database/MovieLens/ml-1m/ra.test";

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
	private static UserCenteredCollaborativeFiltering filtre;

	@BeforeClass
	public static void initData() throws Exception {
		users = MovieLens1MDataReader.findUsersFile(usersFile);// userD.findUsers();
		items = MovieLens1MDataReader.findItemsFile(itemsFile);// itemD.findItems();
		dataBaseEntries = MovieLens1MDataReader.findRatingsFile(learningRatingsFile);
		//System.out.println(dataBaseEntries);
		filtre = new UserCenteredCollaborativeFiltering(users, items, dataBaseEntries);
	}

	/*
	@Test
	public void simPearsonTest() {
		User activeUser = users.get(1227);
		System.out.println("----------------TestPearson----------------------");
		Object simMap = filtre.simPearson(activeUser);
		System.out.println("simPearson= "+simMap);

	}

	// Looking for Neighborhood
	@Test
	public void neighborhoodTest() {
		User activeUser = users.get(0);
		System.out.println("----------------TestNeighborhood----------------------");
		Map<User, Double> simMap = filtre.simPearson(activeUser);
		ArrayList<User> userList = filtre.neighborhood(simMap, activeUser);
		System.out.println(userList);
	}
	*/
/*
	@Test
	public void oneUserRatingsQualityTest() {
		User activeUser = users.get(0);
		oneUserRatingsQuality(activeUser);
	}
*/
	// Rating estimation
	public List<RealAndPrediction> oneUserRatingsQuality(User activeUser) {
		Map<User, Double> simMap = filtre.calculateUsersSimilarities(activeUser);
		ArrayList<User> similarUserList = filtre.neighborhood(simMap,activeUser);
		List<Rating> allEstimations = filtre.userRatingsEstimation(activeUser, similarUserList,simMap);
		/* begin Quality Test */
		List<Rating> userRealRatings = MovieLens1MDataReader.findUserRatings(testRatingsFile, activeUser.getIdUser());
		//System.out.println("user " + activeUser.getIdUser() + " has "+ userRealRatings.size() + " ratings");
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

			realsAndPredicted.add(new RealAndPrediction(realRating,	predictedRating));
		}
		try {
			double mae = Mathematics.mae(realsAndPredicted);
			double rmse = Mathematics.rmse(realsAndPredicted);
			System.out.format("User %d \t %d \t %.3f \t %.3f\n", activeUser.getIdUser(), userRealRatings.size(), mae, rmse);

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
			List<RealAndPrediction> oneUserPrediction = oneUserRatingsQualityTest(u);
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
	}
	
}
