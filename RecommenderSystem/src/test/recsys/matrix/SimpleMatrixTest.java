package test.recsys.matrix;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.MapMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.matrix.SimpleMatrix;
import com.recsys.matrix.SimpleVector;

public class SimpleMatrixTest {
	// matrix with 3 columns and 2 rows
	SimpleMatrix mat=new SimpleMatrix(10,3,5);
	// similarity Map
	Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	// estimation Map
	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	// user list array
	ArrayList<Integer> userList = new ArrayList<Integer>();
	//threashold used to recommend high ranked items
	double THREASHOLD=5;

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

	

	@Test
	public final void testSimPearson() {
		simMap = mat.simPearson(0);
		System.out.println("simPearson = "+simMap);
	}

	@Test
	public final void testNeighborhood() {
		//looking for neighborhood
    	userList=mat.neighborhood(simMap,2);
    	System.out.println("Neighborhood list");	    	
    	System.out.println(userList);
	}

	@Test
	public final void testEstimation() {
		//calculate estimated ratings for unrated items
    	System.out.println("Rating estimation");
    	estimMap=mat.estimation(0, userList);
    	System.out.println(estimMap);
    	
	}

	@Test
	public final void testRecommendation() {
		//print items recommended by the system
    	System.out.println("Recommending Items");	
    	mat.Recommendation(estimMap, THREASHOLD);
    }

}
