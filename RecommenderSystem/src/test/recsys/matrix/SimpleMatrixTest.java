package test.recsys.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.recsys.matrix.SimpleMatrix;

public class SimpleMatrixTest {
	// matrix with 3 columns and 2 rows
	SimpleMatrix mat=new SimpleMatrix(10,5,5);
	// similarity Map
	Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	// estimation Map
	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	// user list array
	ArrayList<Integer> userList = new ArrayList<Integer>();
	//threashold used to recommend high ranked items
	double THREASHOLD=5;
	int ACTIVEUSER=0;
	int NEIGHBORNUMBER=4;

	@Before
	public void InitialisationTest() throws Exception {
		
    	System.out.println(mat.size());
    	
    	// Print the matrix
    	System.out.println("Matrix:");
    		for(int rows=0;rows<mat.getRowsNumber();rows++){
    			for(int cols=0;cols<mat.getColumnsNumber();cols++){
    			System.out.print(mat.get(rows,cols)+"\t");
    		}
    		System.out.println();
    	}
    	
   	    
    	
    	
    	
    	
    	
		
	}

	
/*
	@Test
	public final void testSimPearson() {
		simMap = mat.simPearson(ACTIVEUSER);
		System.out.println("simPearson = "+simMap);
	}

	@Test
	public final void testNeighborhood() {
		simMap = mat.simPearson(ACTIVEUSER);
		System.out.println("simPearson = "+simMap);
		//looking for neighborhood
    	userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
    	System.out.println("Neighborhood list");	    	
    	System.out.println(userList);
	}

	@Test
	public final void testEstimation() {
		simMap = mat.simPearson(ACTIVEUSER);
		System.out.println("simPearson = "+simMap);
		//looking for neighborhood
    	userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
    	System.out.println("Neighborhood list");	    	
    	System.out.println(userList);
		//calculate estimated ratings for unrated items
    	System.out.println("Rating estimation");
    	estimMap=mat.estimation(ACTIVEUSER, userList);
    	System.out.println(estimMap);
    	
	}

	@Test
	public final void testRecommendation() {
		simMap = mat.simPearson(ACTIVEUSER);
		System.out.println("simPearson = "+simMap);
		//looking for neighborhood
    	userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
    	System.out.println("Neighborhood list");	    	
    	System.out.println(userList);
		//calculate estimated ratings for unrated items
    	System.out.println("Rating estimation");
    	estimMap=mat.estimation(ACTIVEUSER, userList);
    	System.out.println(estimMap);
		//print items recommended by the system
    	System.out.println("Recommending Items");	
    	mat.Recommendation(estimMap, THREASHOLD);
    }
*/
}
