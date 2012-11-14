package test.recsys.DomainDAO;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.MovieLens100KDataReader;

public class MoveieLens100KDataReaderTest {
	private static String learningRatingsFile = "database/MovieLens/ml-100K/ua.base";
	private static String usersFile = "database/MovieLens/ml-100K/u.user";
	private static String itemsFile = "database/MovieLens/ml-100K/u.item";
	private static String testRatingsFile = "database/MovieLens/ml-100K/ua.test";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindUsersFile() {
		List<User> users = MovieLens100KDataReader.findUsersFile(usersFile);
		assertEquals(943, users.size());
	}

	@Test
	public final void testFindItemsFile() {
		List<Item> items = MovieLens100KDataReader.findItemsFile(itemsFile);
		assertEquals(1682, items.size());
	}

	@Test
	public final void testFindLearningRatingsFile() {
		List<Rating> ratings = MovieLens100KDataReader.findRatingsFile(learningRatingsFile);
		assertEquals(90570, ratings.size());
	}

	@Test
	public final void testFindTestRatingsFile() {
		List<Rating> ratings = MovieLens100KDataReader.findRatingsFile(testRatingsFile);
		assertEquals(9430, ratings.size());
	}
	
	@Test
	public final void testFindUserRatings() {
		List<Rating> ratings = MovieLens100KDataReader.findUserRatings(learningRatingsFile,1);
		System.out.println(ratings);
	}

}
