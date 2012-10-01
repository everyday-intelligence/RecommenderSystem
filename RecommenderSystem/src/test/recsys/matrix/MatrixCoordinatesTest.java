package test.recsys.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.recsys.matrix.MatrixCoordinates;

public class MatrixCoordinatesTest {

	private int row = 5;
	private int column = 10;
	MatrixCoordinates mc = new MatrixCoordinates(row, column);
	
	

	@Test
	public final void testGetRow() {
		assertEquals(row, mc.getRow());
	}

	@Test
	public final void testGetColumn() {
		assertEquals(column, mc.getColumn());
	}

	@Test
	public final void testEqualsObject() {
		assertTrue(mc.equals(new MatrixCoordinates(row, column)));
	}

}
