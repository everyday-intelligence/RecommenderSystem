package com.recsys.matrix;

public abstract class AbstractMatrix {

	protected abstract int getColumnsNumber();
	protected abstract int getRowsNumber();
	protected abstract Double get(int i, int j);
	protected abstract boolean set(int i, int j, Double vals);



}
