package com.recsys.matrix;

import java.util.HashMap;
import java.util.Map;

public class MapMatrix extends AbstractMatrix {


	Map<MatrixCoordinates,Double> matrix = new HashMap<MatrixCoordinates,Double>();

	public MapMatrix(int columnsNumber, int rowsNumber) {
		super(columnsNumber, rowsNumber);
	}

	@Override
	protected Double get(int row, int col) {
		return matrix.get(new MatrixCoordinates(row, col));
	}

	@Override
	protected void set(int row, int col, Double val) {
			matrix.put(new MatrixCoordinates(row, col),val);
	}

}
