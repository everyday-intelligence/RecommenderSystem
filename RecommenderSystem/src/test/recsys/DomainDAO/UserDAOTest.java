package test.recsys.DomainDAO;

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.User;
import com.recsys.DomainDAO.UserDAO;

public class UserDAOTest {

	private EntityManagerFactory emf=Persistence.createEntityManagerFactory("RecommenderSystem");	
	UserDAO userD=new UserDAO(emf);
	private EntityManager em=userD.getEntityManager();
	User u1=new User(123);
	User u2=new User(124);
	
	@Before
	public void setUp() throws Exception {
	
		System.out.println("*********************Test starts******************");
		
	
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("*********************Test ends******************");
		System.out.println();
	}
/*
	@Test
	public void testCreate() {
		System.out.println("Ajout d'un utilisateur");
		//userD.create(u1);
		//userD.create(u2);
	}

	@Test
	public void testEdit() throws Exception{
		System.out.println("Modifier les données d'un utilisateur");
		userD.edit(u1);
	}

	@Test
	public void testFindUsers() {
		System.out.println("Recherche de tous les utilisateurs");
		System.out.println(userD.findUsers());
	}

	@Test
	public void testFindUser() {
		System.out.println("Recherche d'un utilisateur");
		System.out.println(userD.findUser((long)123).getRatingList());
	}

	@Test
	public void testDestroy() {
		System.out.println("Suppression d'un utilisateur");
		//userD.destroy((long)123);
		//userD.destroy((long)124);
	}
	*/
	

}
