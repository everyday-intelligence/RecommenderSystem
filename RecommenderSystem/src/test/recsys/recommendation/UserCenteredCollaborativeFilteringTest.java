package test.recsys.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.SimpleMatrix;
import com.recsys.recommendation.Recommendation;
import com.recsys.recommendation.UserCenteredCollaborativeFiltering;

public class UserCenteredCollaborativeFilteringTest {
		
	User u1=new User(120);
	User u2=new User(121);
	User u3=new User(122);
	User u4=new User(123);
	User u5=new User(124);
	
	
	Item i1=new Item(000);
	Item i2=new Item(001);
	Item i3=new Item(002);
	Item i4=new Item(003);
	
	private List<User> users = new ArrayList<User>(){{add(u1);add(u2);add(u3);add(u4);add(u5);}};
	private List<Item> items = new ArrayList<Item>(){{add(i1);add(i2);add(i3);add(i4);}};
		
	UserCenteredCollaborativeFiltering filtre=new UserCenteredCollaborativeFiltering(users,items);
	
	User activeUser=u1;
	int K=2;
	double THREASHOLD=5;
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
			//1st column
		filtre.getDataMatrix().set(0, 0, 4.0);
		filtre.getDataMatrix().set(1, 0, 4.0);
		filtre.getDataMatrix().set(2, 0, 3.0);
		filtre.getDataMatrix().set(3, 0, 5.0);
		filtre.getDataMatrix().set(4, 0, 4.0);
    		//2nd column
		filtre.getDataMatrix().set(0, 1, 2.0);  
		filtre.getDataMatrix().set(1, 1, 4.0);
		filtre.getDataMatrix().set(2, 1, 3.0);
		filtre.getDataMatrix().set(3, 1, 5.0);
		filtre.getDataMatrix().set(4, 1, 3.5);
    		//3rd column
		filtre.getDataMatrix().set(0, 2, 0.0);  
		filtre.getDataMatrix().set(1, 2, 4.0);
		filtre.getDataMatrix().set(2, 2, 3.0);
		filtre.getDataMatrix().set(3, 2, 5.0);
		filtre.getDataMatrix().set(4, 2, 4.0);
			//4th column
		filtre.getDataMatrix().set(0, 3, 2.0);
		filtre.getDataMatrix().set(1, 3, 2.0);
		filtre.getDataMatrix().set(2, 3, 3.0);
		filtre.getDataMatrix().set(3, 3, 1.0);
		filtre.getDataMatrix().set(4, 3, 5.0);
    	
		
		
		System.out.println("Matrix:");
		for(int row=0;row<filtre.getDataMatrix().getRowsNumber();row++){
			for(int col=0;col<filtre.getDataMatrix().getColumnsNumber();col++){
			
				System.out.print(filtre.getDataMatrix().get(row, col)+"\t");
				
			}
			
			System.out.println();
		}
		
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
    	userList=filtre.neighborhood(simMap, K, activeUser);
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
    	userList=filtre.neighborhood(simMap,K,activeUser);
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
