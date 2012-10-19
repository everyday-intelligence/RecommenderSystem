package com.recsys.matrix;

import java.io.Serializable;

public abstract class AbstractMatrix  implements Serializable{


	protected final int columnsNumber;
	protected final int rowsNumber;
		
	public AbstractMatrix(int rowsNumber,int columnsNumber) {
		super();
		this.columnsNumber = columnsNumber;
		this.rowsNumber = rowsNumber;
	}
	
	public abstract Double get(int row, int col);
	public abstract void set(int row, int col, Double vals);

	public int getColumnsNumber() {
		return columnsNumber;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	public abstract AbstractVector getRow(int rowNumber);
	public abstract AbstractVector getColumn(int colNumber);
	
	
	public MatrixCoordinates size(){
		return new MatrixCoordinates(rowsNumber, columnsNumber);
	}

}
