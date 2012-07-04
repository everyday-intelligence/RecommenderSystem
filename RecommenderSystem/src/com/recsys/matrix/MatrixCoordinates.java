package com.recsys.matrix;

public class MatrixCoordinates{

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrixCoordinates other = (MatrixCoordinates) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MatrixCoordinates [row=" + row + ", column=" + column + "]";
	}

	
	
	
}
