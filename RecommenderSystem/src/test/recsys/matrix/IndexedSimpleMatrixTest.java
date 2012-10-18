package test.recsys.matrix;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recsys.matrix.IndexedSimpleMatrix;

public class IndexedSimpleMatrixTest {
	// matrix with 3 columns and 2 rows

	IndexedSimpleMatrix mat;

	@Before
	public void InitialisationTest() throws Exception {
		List<Long> rl = new ArrayList<Long>();
		for (int i = 0; i < 10; i++) {
			rl.add((long) i);
		}
		List<Long> cl = new ArrayList<Long>();
		for (int i = 0; i < 5; i++) {
			cl.add((long) i);
		}
		mat = new IndexedSimpleMatrix(rl, cl);
		System.out.println(mat.size());
		// Print the matrix

		System.out.println("Matrix:");
		for (int rows = 0; rows < mat.getRowsNumber(); rows++) {
			for (int cols = 0; cols < mat.getColumnsNumber(); cols++) {
				mat.set(rows, cols, (double) (rows * cols));
			}
			System.out.println();
		}

	}

	@Test
	public void testGetSet() {
		// Print the matrix
		System.out.println("Matrix:");
		for (int rows = 0; rows < mat.getRowsNumber(); rows++) {
			for (int cols = 0; cols < mat.getColumnsNumber(); cols++) {
				System.out.print(mat.get(rows, cols) + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void testGetCol() {
		long idCol = 2;
		System.out.println("Matrix col :" + idCol);
		System.out.println(mat.getColumn(idCol));

	}

	@Test
	public void testGetRow() {
		long idRow = 3;
		System.out.println("Matrix row :" + idRow);
		System.out.println(mat.getColumn(idRow));

	}

	/*
	 * @Test public final void testSimPearson() { simMap =
	 * mat.simPearson(ACTIVEUSER); System.out.println("simPearson = "+simMap); }
	 * 
	 * @Test public final void testNeighborhood() { simMap =
	 * mat.simPearson(ACTIVEUSER); System.out.println("simPearson = "+simMap);
	 * //looking for neighborhood
	 * userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
	 * System.out.println("Neighborhood list"); System.out.println(userList); }
	 * 
	 * @Test public final void testEstimation() { simMap =
	 * mat.simPearson(ACTIVEUSER); System.out.println("simPearson = "+simMap);
	 * //looking for neighborhood
	 * userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
	 * System.out.println("Neighborhood list"); System.out.println(userList);
	 * //calculate estimated ratings for unrated items
	 * System.out.println("Rating estimation");
	 * estimMap=mat.estimation(ACTIVEUSER, userList);
	 * System.out.println(estimMap);
	 * 
	 * }
	 * 
	 * @Test public final void testRecommendation() { simMap =
	 * mat.simPearson(ACTIVEUSER); System.out.println("simPearson = "+simMap);
	 * //looking for neighborhood
	 * userList=mat.neighborhood(simMap,NEIGHBORNUMBER,ACTIVEUSER);
	 * System.out.println("Neighborhood list"); System.out.println(userList);
	 * //calculate estimated ratings for unrated items
	 * System.out.println("Rating estimation");
	 * estimMap=mat.estimation(ACTIVEUSER, userList);
	 * System.out.println(estimMap); //print items recommended by the system
	 * System.out.println("Recommending Items"); mat.Recommendation(estimMap,
	 * THREASHOLD); }
	 */
}
