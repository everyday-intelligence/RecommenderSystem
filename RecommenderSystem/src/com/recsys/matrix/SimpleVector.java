package com.recsys.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleVector extends AbstractVector{

	private Double[] vector;

	public SimpleVector(Double[] vector) {
		super(vector.length);
		this.vector = vector;
	}
	public SimpleVector(int size) {
		super(size);
		this.vector = new Double[size];
	}
	public int size(){
		return vector.length;
	}
	@Override
	public Double get(int index) {
		return vector[index];
	}
	@Override
	public void set(int index, Double val) {
		vector[index]=val;		
	}
	@Override
	public String toString() {
		return "SimpleVector ["
				+ (vector != null ? "vector=" + Arrays.toString(vector) : "")
				+ "]";
	}
	
	public List<Double> toList(){
		return Arrays.asList(vector);
	}
	
}
