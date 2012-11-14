package test.recsys.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.Recommendation;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UserCenteredCollaborativeFilteringTest {


	private static String learningRatingsFile = "database/MovieLens/ml-100K/ua.base";
	private static String usersFile = "database/MovieLens/ml-100K/u.user";
	private static String itemsFile = "database/MovieLens/ml-100K/u.item";
	private static String testRatingsFile = "database/MovieLens/ml-100K/ua.test";
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
	private List<User> users = MovieLens100KDataReader.findUsersFile(usersFile);// userD.findUsers();
	private List<Item> items = MovieLens100KDataReader.findItemsFile(itemsFile);// itemD.findItems();
	private List<Rating> dataBaseEntries = MovieLens100KDataReader.findRatingsFile(learningRatingsFile);
	UserCenteredCollaborativeFiltering filtre = new UserCenteredCollaborativeFiltering(
			users, items, dataBaseEntries);

	public User activeUser = users.get(0);

	// similarity Map
	Map<User, Double> simMap = new HashMap<User, Double>();
	// estimation Map
	List<Rating> allEstimations = new ArrayList<Rating>();
	// user list array
	ArrayList<User> userList = new ArrayList<User>();
	// RecommendationList
	List<Recommendation> recommendationList = new ArrayList<Recommendation>();

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void recommendTest() {
		System.out.println("----------------Test Recommend----------------------");
		System.out.println("estimating for user " + activeUser.getIdUser());
		System.out.println(users.size() + " users in DB ");
		System.out.println(items.size() + " items in DB ");
		recommendationList = filtre.recommend(activeUser);
		System.out.println(" there is "+recommendationList.size()+" candidate (rating != 0)");

	}

	// Rating estimation
	@Test
	public void ratingsEstimationTest() {
		System.out.println("----------------TestEstimation----------------------");
		System.out.println("estimating for user " + activeUser.getIdUser());
		System.out.println(users.size() + " users in DB ");
		System.out.println(items.size() + " items in DB ");
		simMap = filtre.calculateUsersSimilarities(activeUser);
		userList = filtre.neighborhood(simMap, activeUser);
		System.out.println("Rating estimation");
		allEstimations = filtre.userRatingsEstimation(activeUser, userList,simMap);
	}

	// Similarity: Pearson's correlation coefficient
	@Test
	public void simPearsonTest() {
		System.out.println("----------------TestPearson----------------------");
		simMap = filtre.calculateUsersSimilarities(activeUser);
		// System.out.println("simPearson= "+simMap);

	}

	// Looking for Neighborhood
	@Test
	public void neighborhoodTest() {
		System.out.println("----------------TestNeighborhood----------------------");
		simMap = filtre.calculateUsersSimilarities(activeUser);
		userList = filtre.neighborhood(simMap, activeUser);
	}

}
