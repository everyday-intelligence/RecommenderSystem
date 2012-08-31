package test.recsys.DomainDAO;

import static org.junit.Assert.fail;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.User;
import com.recsys.DomainDAO.UserDAO;

public class UserDAOTest {

	private EntityManagerFactory emf;
	
	UserDAO userD=new UserDAO(emf);
	User u1=new User(123);
	
	
	@Before
	public void setUp() throws Exception {
	
		System.out.println("Test start:");
	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		System.out.println("Ajout d'un utilisateur");
	}

	@Test
	public void testEdit() {
		fail("Not yet implemented");
	}

	@Test
	public void testDestroy() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUser() {
		fail("Not yet implemented");
	}

}
