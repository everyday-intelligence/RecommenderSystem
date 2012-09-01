package test.recsys.DomainDAO;

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.DomainDAO.ItemDAO;

public class ItemDAOTest {

	
	private EntityManagerFactory emf=Persistence.createEntityManagerFactory("RecommenderSystem");	
	ItemDAO itemD=new ItemDAO(emf);
	private EntityManager em=itemD.getEntityManager();
	Item i1=new Item(001);
	Item i2=new Item(002);
	
	
	@Before
	public void setUp() throws Exception {
	
		System.out.println("*********************Test starts******************");
	
	}
	

	@After
	public void tearDown() throws Exception {
		
		System.out.println("*********************Test ends******************");
		System.out.println();
		
	}

	@Test
	public void testCreate() {
		System.out.println("Ajout d'un item");
		itemD.create(i1);
		itemD.create(i2);
	}

	@Test
	public void testEdit() throws Exception {
		System.out.println("Modifier les données d'un item");
		itemD.edit(i1);
	}

	@Test
	public void testFindItems() {
		System.out.println("Recherche de tous les items");
		System.out.println(itemD.findItems());
	}

	@Test
	public void testFindItem() {
		System.out.println("Recherche d'un item");
		System.out.println(itemD.findItem((long)001));
	}
	
	@Test
	public void testDestroy() {
		System.out.println("Suppression d'un item");
		//itemD.destroy((long)001);
		//itemD.destroy((long)002);
	}

}
