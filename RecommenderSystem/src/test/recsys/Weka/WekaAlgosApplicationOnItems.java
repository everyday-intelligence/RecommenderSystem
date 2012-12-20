package test.recsys.Weka;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.DomainDAO.MovieLens100KDataReader;

import weka.core.Attribute;
import weka.core.EuclideanDistance;
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
		System.out.println(itemsDataset.toSummaryString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDuplicate() {
		System.out.println(itemsDataset.toSummaryString());
		System.out.println(itemsDataset.instance(0).value(3));
	}
	
	
	@Test
	public final void testKNN() {
		LinearNNSearch knn = new LinearNNSearch(itemsDataset);
		try {
			//ManhattanDistance df = new ManhattanDistance(itemsDataset);
			EuclideanDistance df = new EuclideanDistance(itemsDataset);
			df.setDontNormalize(false);
			df.setAttributeIndices("3,6-last");
			//System.out.println(df.getAttributeIndices());
			knn.setDistanceFunction(df);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration itms = itemsDataset.enumerateInstances();
		while(itms.hasMoreElements()){
			Instance in = (Instance) itms.nextElement();
			 try {
				Instances nearestNeighbour = knn.kNearestNeighbours(in,15);
				for(int i=0;i<nearestNeighbour.numInstances();i++){
					Instance in2 = nearestNeighbour.instance(i);
					double[] distances = knn.getDistances();	
					//System.out.println(distances.length);
					//System.out.println(in.attribute(0).value((int) in.value(0))+" _ "+(in2.attribute(0).value((int) in2.value(0)))+" = "+distances[0]);				
				}
							} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
