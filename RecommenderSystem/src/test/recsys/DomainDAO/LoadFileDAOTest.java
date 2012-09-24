package test.recsys.DomainDAO;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.LoadFile;

public class LoadFileDAOTest {
	private static String learningRatingsFile = "database/ua.base";
	private static String usersFile = "database/u.user";
	private static String itemsFile = "database/u.item";
	private static String testRatingsFile = "database/ua.test";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindUsersFile() {
		List<User> users = LoadFile.findUsersFile(usersFile);
		assertEquals(943, users.size());
	}

	@Test
	public final void testFindItemsFile() {
		List<Item> items = LoadFile.findItemsFile(itemsFile);
		assertEquals(1682, items.size());
	}

	@Test
	public final void testFindLearningRatingsFile() {
		List<Rating> ratings = LoadFile.findRatingsFile(learningRatingsFile);
		assertEquals(90570, ratings.size());
	}

	@Test
	public final void testFindTestRatingsFile() {
		List<Rating> ratings = LoadFile.findRatingsFile(testRatingsFile);
		assertEquals(9430, ratings.size());
	}
	
	@Test
	public final void testFindUserRatings() {
		List<Rating> ratings = LoadFile.findUserRatings(learningRatingsFile,1);
		System.out.println(ratings);
	}

}
