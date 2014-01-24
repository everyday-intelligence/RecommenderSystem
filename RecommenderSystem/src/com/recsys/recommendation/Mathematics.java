package com.recsys.recommendation;

import java.util.Iterator;
import java.util.List;

import com.recsys.quality.RealAndPrediction;

public class Mathematics {

	// calculates average for standard deviation
	public static double average(List<Double> list) {
		if(list.isEmpty()){
			//System.out.println("emptyyyyyyyyyyyyyyyyy");
			return 0;
		}

		double avg = 0;
		for (double value : list) {
			avg += value;
		}

		
		return avg / list.size();

	}
	
	// Standard derivation
	public static double standardDeviation(List<Double> list) {

		double sd = 0;
		double av = average(list);
		for (double value : list) {
			sd += Math.pow((value - av), 2);
		}

		return Math.sqrt(sd / list.size());
	}

	public static double mae(List<RealAndPrediction> predictionsCouples)
			throws Exception {

	
		if (predictionsCouples.size() == 0) {
			throw new Exception("empty values");
		}
		double error = 0;
		Iterator<RealAndPrediction> it = predictionsCouples.iterator();
		while(it.hasNext()) {
			RealAndPrediction predictionsCouple = it.next();
			//System.out.println("predicted "+ predictionsCouples.get(i).getPredictedValue()+" real "+ predictionsCouples.get(i).getRealValue());
			//System.out.format("predicted %f \t real %f \n", predictionsCouples.get(i).getPredictedValue(), predictionsCouples.get(i).getRealValue());
			error += Math.abs(predictionsCouple.getPredictedValue() - predictionsCouple.getRealValue());
		}
		/*
		if(new Double(error / predictionsCouples.size()).isNaN()){
			System.out.println(predictionsCouples);
		}*/
		return error / predictionsCouples.size();
	}

	public static double rmse(List<RealAndPrediction> predictionsCouples)
			throws Exception {

		
		if (predictionsCouples.size() == 0) {
			throw new Exception("empty values");
		}
		double error = 0;
		for(RealAndPrediction predictionsCouple:predictionsCouples) {
			error += Math.pow((predictionsCouple.getPredictedValue() - predictionsCouple.getRealValue()),2);
		}
		error /= predictionsCouples.size();
		//System.out.println(error);
		return Math.sqrt(error);
	}

}
