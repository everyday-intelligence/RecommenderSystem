package com.recsys.matrix;

public abstract class AbstractVector {

	public int length;
	public abstract  Double get(int index);
	public abstract void set(int index, Double val);
	public AbstractVector(int length) {
		super();
		this.length = length;
	}

}
