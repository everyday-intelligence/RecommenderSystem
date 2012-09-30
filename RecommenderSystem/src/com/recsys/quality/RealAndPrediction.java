package com.recsys.quality;

public class RealAndPrediction {

	private double realValue;
	private double predictedValue;
	public RealAndPrediction(double realValue, double predictedValue) {
		super();
		this.realValue = realValue;
		this.predictedValue = predictedValue;
	}
	public double getRealValue() {
		return realValue;
	}
	public double getPredictedValue() {
		return predictedValue;
	}
	@Override
	public String toString() {
		return "realValue=" + realValue
				+ ", predictedValue=" + predictedValue + "";
	}
		
}
