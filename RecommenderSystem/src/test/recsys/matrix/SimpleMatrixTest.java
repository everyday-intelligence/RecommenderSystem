package test.recsys.matrix;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.recsys.matrix.MapMatrix;
import com.recsys.matrix.MatrixFactory;
import com.recsys.matrix.SimpleMatrix;

public class SimpleMatrixTest {
	SimpleMatrix m = null;
	int nbR = 3;
	int nbC = 3;

	@Before
	public void setUp() throws Exception {
		m = new SimpleMatrix(nbR, nbC);
		for (int i = 0; i < m.getRowsNumber(); i++) {
			for (int j = 0; j < m.getRowsNumber(); j++) {
				Double v = Math.ceil(100*Math.random());
				if(v>0){//simuler une matrice avec presque 90% des valeurs à zero
					m.set(i, j, v);
				}else{
					m.set(i, j, 0d);
				}
			}
		}
	m.set(nbR/2, nbC/2, new Double(20));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAverage() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testStandardDeviation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSimPearson() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNeighborhood() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEstimation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRecommendation() {
		fail("Not yet implemented"); // TODO
	}

}
