package test.recsys.DomainDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.RatingDAO;

public class RatingDAOTest {
	
	private EntityManagerFactory emf=Persistence.createEntityManagerFactory("RecommenderSystem");	
	RatingDAO ratingD=new RatingDAO(emf);
	private EntityManager em=ratingD.getEntityManager();
	Item i1=new Item(001);
	User u1=new User(123);
	Item i2=new Item(002);
	User u2=new User(124);
	Rating r1=new Rating(3,i1,u1);
	Rating r2=new Rating(4,i2,u2);
	Rating r3=new Rating(2,i2,u1);
	
	@Before
	public void setUp() throws Exception {
		System.out.println("test starts");
	}

	@After
	public void tearDown() throws Exception {
	}
/*
	@Test
	public void testCreate() {
	
		System.out.println("Ajout d'un rating");
		ratingD.create(r1);
		ratingD.create(r2);
		ratingD.create(r3);
		
	}
	
	@Test
	public void testFindRatings() {
	
		System.out.println("Afficher les ratings");
		System.out.println(ratingD.findRatings());
		
	}
	*/
	
	

}
