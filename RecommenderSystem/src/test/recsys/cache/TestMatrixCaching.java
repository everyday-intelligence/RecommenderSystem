package test.recsys.cache;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.jcs.JCS;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.cache.RecSysCache;
import com.recsys.matrix.IndexedSimpleMatrix;

public class TestMatrixCaching {
	
	static JCS jcs;
	static IndexedSimpleMatrix mat;
	

	@BeforeClass
	public static void InitialisationTest() throws Exception {
		jcs = RecSysCache.getJcs();

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

	@Before
	public void setUp() throws Exception {
		jcs.put("mat0", mat);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		IndexedSimpleMatrix mat2 = (IndexedSimpleMatrix) jcs.get("mat0");
		assertNotNull(mat2);
		assertEquals(mat.getRowsNumber(), mat2.getRowsNumber());
		assertEquals(mat.getColumnsNumber(), mat2.getColumnsNumber());
		jcs.dispose();
		
	}

}
