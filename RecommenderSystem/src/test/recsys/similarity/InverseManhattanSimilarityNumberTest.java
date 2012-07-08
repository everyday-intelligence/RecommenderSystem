package test.recsys.similarity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.similarity.InverseManhattanSimilarityNumber;

public class InverseManhattanSimilarityNumberTest {

	private List<Double> l1;
	private List<Double> l2;
	private int length = 10;
	@Before
	public void setUp() throws Exception {
		l1 = new ArrayList<Double>();
		l2 = new ArrayList<Double>();
		for(int i=0;i<length ;i++){
			l1.add(new Double(i));
			l2.add(new Double(i+1));
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMeasureSimilarityListOfTListOfT() {
		InverseManhattanSimilarityNumber<Double> manhattanSim = new InverseManhattanSimilarityNumber<Double>();
		Double sim = manhattanSim.measureSimilarity(l1, l2);
		assertEquals(new Double(1d/(length*1)),sim);
	}

}
