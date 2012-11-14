package test.recsys.DomainDAO;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens1MDataReader;

public class MoveieLens1MDataReaderTest {
	private static String learningRatingsFile = "database/MovieLens/ml-1m/ratings.dat";
	private static String usersFile = "database/MovieLens/ml-1m/users.dat";
	private static String itemsFile = "database/MovieLens/ml-1m/movies.dat";
	//private static String testRatingsFile = "database/MovieLens/ml-1m/ua.test";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindUsersFile() {
		List<User> users = MovieLens1MDataReader.findUsersFile(usersFile);
		assertEquals(6040, users.size());
	}

	@Test
	public final void testFindItemsFile() {
		List<Item> items = MovieLens1MDataReader.findItemsFile(itemsFile);
		assertEquals(3883, items.size());
	}

	@Test
	public final void testFindLearningRatingsFile() {
		List<Rating> ratings = MovieLens1MDataReader.findRatingsFile(learningRatingsFile);
		assertEquals(1000209, ratings.size());
	}

	/*
	@Test
	public final void testFindTestRatingsFile() {
		List<Rating> ratings = MoveieLens1MDataReader.findRatingsFile(testRatingsFile);
		assertEquals(9430, ratings.size());
	}
	*/
	@Test
	public final void testFindUserRatings() {
		List<Rating> ratings = MovieLens1MDataReader.findUserRatings(learningRatingsFile,1);
		System.out.println(ratings);
	}

}
