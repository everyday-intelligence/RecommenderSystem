package com.recsys.matrix;

public abstract class AbstractMatrix {


	private final int columnsNumber;
	private final int rowsNumber;
		
	public AbstractMatrix(int columnsNumber, int rowsNumber) {
		super();
		this.columnsNumber = columnsNumber;
		this.rowsNumber = rowsNumber;
	}
	
	protected abstract Double get(int row, int col);
	protected abstract void set(int row, int col, Double vals);

	public int getColumnsNumber() {
		return columnsNumber;
	}

	public int getRowsNumber() {
		return rowsNumber;
	}

	

}
