package test.recsys.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.recsys.matrix.AbstractVector;
import com.recsys.matrix.MapMatrix;
import com.recsys.matrix.MapVector;

public class MapMatrixTest {

	MapMatrix m = null;
	int nbR = 3;
	int nbC = 3;


	@Before
	public final void setUp() {
		System.out.println("before");
			m = new MapMatrix(nbR, nbC);
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
	
	
	@Test
	public final void testRealSize(){
		System.out.println("real size = "+m.getRealSize()+ " instead of "+m.getRowsNumber()*m.getColumnsNumber());
	}

	@Test
	public final void testSize(){
		System.out.println("matrix size = "+m.size());
	}
	@Test
	public final void testGetRow() {
		System.out.println(m);
		int row = 1;
		System.out.println("getting row "+row);
		AbstractVector v = m.getRow(row);
		System.out.println("result");
		System.out.println(v);
		MapVector expected = new MapVector(m.getColumnsNumber());
		for(int i=0;i<m.getColumnsNumber();i++){
			expected.set(i, m.get(1, i));
		}
		System.out.println("expected");
		System.out.println(expected);
		assertTrue(expected.equals(v));
	}
	
	
}
