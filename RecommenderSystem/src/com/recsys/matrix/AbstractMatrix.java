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
	
	public abstract double get(int row, int col);
	public abstract void set(int row, int col, double vals);
	public abstract double get(long rowLabel, long colLabel);
	public abstract void set(long rowLabel, long colLabel, double vals);

	public int getColumnsNumber() {
		return columnsNumber;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	public abstract AbstractVector getRow(int rowNumber);
	public abstract AbstractVector getColumn(int colNumber);
	public abstract AbstractVector getRow(long rowLabel) ;
	public abstract AbstractVector getColumn(long colLabel) ;
	
	public MatrixCoordinates size(){
		return new MatrixCoordinates(rowsNumber, columnsNumber);
	}

}
