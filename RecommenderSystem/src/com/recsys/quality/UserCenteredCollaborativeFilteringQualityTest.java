package com.recsys.quality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.RatingItemChecker;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.RecommendationItemIDChecker;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.ItemDAO;
import com.recsys.DomainDAO.LoadFile;
import com.recsys.DomainDAO.RatingDAO;
import com.recsys.DomainDAO.UserDAO;
import com.recsys.recommendation.Mathematics;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;
import com.recsys.utils.PredicateUtils;

public class UserCenteredCollaborativeFilteringQualityTest {

	private static String learningRatingsFile = "database/ua.base";
	private static String usersFile = "database/u.user";
	private static String itemsFile = "database/u.item";
	private static String testRatingsFile = "database/ua.test";

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
		users = LoadFile.findUsersFile(usersFile);// userD.findUsers();
		items = LoadFile.findItemsFile(itemsFile);// itemD.findItems();
		filtre = new UserCenteredCollaborativeFiltering(users, items);
		dataBaseEntries = LoadFile.findRatingsFile(learningRatingsFile);
		// parcours de la liste des entrées à partir du fichier
		for (Rating entry : dataBaseEntries) {
			filtre.getDataMatrix().set(
					(int) entry.getRatingUser().getIdUser() - 1,
					(int) entry.getRatedItem().getIdItem() - 1,
					entry.getRating());
		}
	}

	// Rating estimation
	public List<RealAndPrediction> oneUserRatingsQualityTest(User activeUser) {
		System.out.println("-----------------------------------------");
		Map<User, Double> simMap = filtre.simPearson(activeUser);
		ArrayList<User> similarUserList = filtre.neighborhood(simMap,
				activeUser);
		List<Rating> allEstimations = filtre.ratingEstimation(
				activeUser, similarUserList);
		/* begin Quality Test */
		List<Rating> userRealRatings = LoadFile.findUserRatings(
				testRatingsFile, activeUser.getIdUser());
		System.out.println("user " + activeUser.getIdUser() + " has "
				+ userRealRatings.size() + " ratings");
		if (userRealRatings.isEmpty()) {
			System.out
					.println("no ratings in test data so no quality measure : exit");
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
		try {
			double mae = Mathematics.mae(realsAndPredicted);
			double rmse = Mathematics.rmse(realsAndPredicted);
			System.out.println("User " + activeUser.getIdUser() + " mae = "
					+ mae);
			System.out.println("User " + activeUser.getIdUser() + " rmse = "
					+ rmse);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return realsAndPredicted;
	}

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

	@Test
	public void allUsersRatingsQualityTestParallel()
			throws InterruptedException, ExecutionException {
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);

		List<RealAndPrediction> allPredictions = new ArrayList<RealAndPrediction>();

		List<Future<List<RealAndPrediction>>> futures = new ArrayList<Future<List<RealAndPrediction>>>();
		for (final User u : users) {
			Callable<List<RealAndPrediction>> callable = new Callable<List<RealAndPrediction>>() {
				public List<RealAndPrediction> call() throws Exception {
					// process your input here and compute the output
					List<RealAndPrediction> output = oneUserRatingsQualityTest(u);
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
