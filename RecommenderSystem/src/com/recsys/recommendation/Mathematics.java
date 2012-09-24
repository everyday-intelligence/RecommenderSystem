package com.recsys.recommendation;

import java.util.List;

import com.recsys.quality.RealAndPrediction;

public class Mathematics {

	// calculates average for standard deviation
	public static int average(List<Double> list) {

		double avg = 0;
		for (double value : list) {

			avg += value;

		}

		return (int) avg / list.size();

	}

	// Standard derivation
	public static double standardDeviation(List<Double> list) {

		double sd = 0;
		for (double value : list) {
			sd += Math.pow((value - average(list)), 2);
		}

		return Math.sqrt(sd / list.size());
	}

	public static double mae(List<RealAndPrediction> predictionsCouples)
			throws Exception {

	
		if (predictionsCouples.size() == 0) {
			throw new Exception("empty values");
		}
		double error = 0;
		for (int i = 0; i < predictionsCouples.size(); i++) {
			error += Math.abs(predictionsCouples.get(i).getPredictedValue() - predictionsCouples.get(i).getRealValue());
		}
		return error / predictionsCouples.size();
	}

	public static double rmse(List<RealAndPrediction> predictionsCouples)
			throws Exception {

		
		if (predictionsCouples.size() == 0) {
			throw new Exception("empty values");
		}
		double error = 0;
		for (int i = 0; i < predictionsCouples.size(); i++) {
			error += Math.pow((predictionsCouples.get(i).getPredictedValue() - predictionsCouples.get(i).getRealValue()),2);
		}
		return Math.sqrt(error / predictionsCouples.size());
	}

}
