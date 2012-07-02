package com.recsys.matrix;

public class MatrixCoordinates {

	private int row;
	private int column;
	public MatrixCoordinates(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	@Override
	public boolean equals(Object obj) {
		MatrixCoordinates mc = (MatrixCoordinates) obj;
		return this.row==mc.getRow()&&this.column==mc.getColumn();
	}
	
	
	
}
