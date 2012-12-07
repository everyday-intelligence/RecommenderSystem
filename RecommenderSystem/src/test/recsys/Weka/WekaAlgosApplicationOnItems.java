package test.recsys.Weka;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.DomainDAO.MovieLens100KDataReader;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.neighboursearch.LinearNNSearch;

public class WekaAlgosApplicationOnItems {
	private static String learningRatingsFile = "database/MovieLens/ml-100K/ua.base";
	private static String usersFile = "database/MovieLens/ml-100K/u.user";
	private static String itemsFile = "database/MovieLens/ml-100K/u.item";
	private static String testRatingsFile = "database/MovieLens/ml-100K/ua.test";

	private Instances itemsDataset;

	@Before
	public void setUp() throws Exception {
		if(itemsDataset == null){
			itemsDataset = MovieLens100KDataReader.fromItemsToWekaDataset(itemsFile);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDuplicate() {
		
	}
	
	
	@Test
	public final void testKNN() {
		LinearNNSearch knn = new LinearNNSearch(itemsDataset);
		try {
			knn.setDistanceFunction(new ManhattanDistance(itemsDataset));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration itms = itemsDataset.enumerateInstances();
		while(itms.hasMoreElements()){
			Instance in = (Instance) itms.nextElement();
			 try {
				Instance nearestNeighbour = knn.nearestNeighbour(in);
				double[] distances = knn.getDistances();				
				System.out.println(in.value(0)+" _ "+nearestNeighbour.value(0)+" = "+distances[0]);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
