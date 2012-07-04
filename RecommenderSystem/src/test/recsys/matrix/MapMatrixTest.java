package test.recsys.matrix;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.recsys.matrix.MapMatrix;

public class MapMatrixTest {

	MapMatrix m = null;
	int nbR = 1000;
	int nbC = 1000;


	@Before
	public final void setUp() {
		System.out.println("before");
			m = new MapMatrix(nbR, nbC);
			for (int i = 0; i < m.getRowsNumber(); i++) {
				for (int j = 0; j < m.getRowsNumber(); j++) {
					Double v = Math.ceil(100*Math.random());
					if(v>90){//simuler une matrice avec presque 90% des valeurs à zero
						m.set(i, j, v);
					}else{
						m.set(i, j, 0d);
					}
				}
			}
		m.set(nbR/2, nbC/2, new Double(20));
		/*
		for (int i = 0; i < m.getRowsNumber(); i++) {
			for (int j = 0; j < m.getRowsNumber(); j++) {
				System.out.print(m.get(i, j)+"\t");
			}
			System.out.println("");
		}
		*/
	}

	@Test
	public final void testSet() {
		m.set(nbR/2, nbC/2, new Double(20));
	}

	@Test
	public final void testGet() {
		Double v = m.get(nbR/2, nbC/2);
		assertEquals(new Double(20), v);
	}


}
