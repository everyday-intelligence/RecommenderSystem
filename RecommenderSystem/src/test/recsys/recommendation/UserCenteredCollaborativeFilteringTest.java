package test.recsys.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;
import com.recsys.DomainDAO.ItemDAO;
import com.recsys.DomainDAO.LoadFile;
import com.recsys.DomainDAO.RatingDAO;
import com.recsys.DomainDAO.UserDAO;
import com.recsys.recommendation.Recommendation;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UserCenteredCollaborativeFilteringTest {
		
	String ratingsFile ="database/u.data";
	String usersFile = "database/u.user";
	String itemsFile = "database/u.item";
	/*
	private EntityManagerFactory emf=Persistence.createEntityManagerFactory("RecommenderSystem");	
	ItemDAO itemD=new ItemDAO(emf);
	UserDAO userD=new UserDAO(emf);
	RatingDAO ratingD=new RatingDAO(emf);
	
	
	private EntityManager em=itemD.getEntityManager();
	private EntityManager emu=userD.getEntityManager();
	private EntityManager emr=ratingD.getEntityManager();
	*/
	private List<User> users = LoadFile.findUsersFile(usersFile);//userD.findUsers();
	private List<Item> items = LoadFile.findItemsFile(itemsFile);//itemD.findItems();
	private List<Rating> dataBaseEntries=LoadFile.findRatingsFile(ratingsFile);	
	UserCenteredCollaborativeFiltering filtre=new UserCenteredCollaborativeFiltering(users,items);
	
	public User activeUser=users.get(0);
	
	// similarity Map
	Map<User,Double> simMap = new HashMap<User,Double>();
	// estimation Map
	Map<Item,Double> estimMap = new HashMap<Item,Double>();
	// user list array
	ArrayList<User> userList = new ArrayList<User>();
	// RecommendationList
	List<Recommendation> recommendationList = new ArrayList<Recommendation>();
	
	@Before
	public void setUp() throws Exception {
		
		//Initialisation
		System.out.println();
		System.out.println("RowsNumber: "+filtre.getDataMatrix().getRowsNumber()+" - ColumnsNumber: "+filtre.getDataMatrix().getColumnsNumber());
			
		//Fill the matrix with zeros
		for (int i = 0; i < filtre.getDataMatrix().getRowsNumber(); i++) {
			for (int j = 0; j < filtre.getDataMatrix().getColumnsNumber(); j++) {
					filtre.getDataMatrix().set(i, j,0.0);
			}
		}
		
		//Fill the matrix with rating values from the database
	/*	for(User usr:users){
			
			for(Rating rating:usr.getRatingList()){
		*/						
		// parcours de la liste des entrées à partir du fichier	
		for(Rating entry:dataBaseEntries){	
			
			filtre.getDataMatrix().set((int)entry.getRatingUser().getIdUser()-1/* users.indexOf(usr) */,(int)entry.getRatedItem().getIdItem()-1/*items.indexOf(rating.getRatedItem())*/,entry.getRating()/*rating.getRating()*/);
			
			
		}
		
		/*	
			}
			
		}
		
		*/
		
		/*
		System.out.println("Matrix:");
		for(int row=0;row<filtre.getDataMatrix().getRowsNumber();row++){
			for(int col=0;col<filtre.getDataMatrix().getColumnsNumber();col++){
			
				System.out.print(filtre.getDataMatrix().get(row, col)+"\t");
				
			}
			
			System.out.println();
		}
		*/
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecommend() {
		System.out.println("----------------Test Recommend----------------------");

		recommendationList=filtre.recommend(activeUser);
		
		for(Recommendation recom:recommendationList){
			System.out.println(recom.toString());
		}
		
	}

	
	// Similarity: Pearson's correlation coefficient 
	@Test
    public void simPearson(){
		System.out.println("----------------TestPearson----------------------");
    
    simMap=filtre.simPearson(activeUser);
    System.out.println("simPearson= "+simMap);
    
    }
        
    
    // Looking for Neighborhood
    @Test
    public void neighborhood(){
		System.out.println("----------------TestNeighborhood----------------------");

    	simMap=filtre.simPearson(activeUser);
    	System.out.println("simPearson = "+simMap);
    	userList=filtre.neighborhood(simMap, activeUser);
    	System.out.println("Neighbor list");
    	for(User user:userList){
    	System.out.println(user);
    	}
    	
    }
    
    
    // Rating estimation 
    @Test
    public void estimation(){
		System.out.println("----------------TestEstimation----------------------");
    	simMap = filtre.simPearson(activeUser);
		System.out.println("simPearson = "+simMap);
		//looking for neighborhood
    	userList=filtre.neighborhood(simMap,activeUser);
    	System.out.println("Neighbor list");	    	
    	for(User user:userList){
    	System.out.println(user);
    	}
		//calculate estimated ratings for unrated items
    	System.out.println("Rating estimation");
    	estimMap=filtre.estimation(activeUser, userList);
    	System.out.println(estimMap);
    	
    }
	
	
}
