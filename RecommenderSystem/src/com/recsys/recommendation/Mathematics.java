package com.recsys.recommendation;

import java.util.ArrayList;

public class Mathematics {
	
	// calculates average for standard deviation
    public static int average(ArrayList<Double> list){
    	
    	double avg=0;
    	for(double value: list){
    		
    		avg+=value;
    		
    	}
    	
    	return (int)avg/list.size();
    	
    }
    
    //Standard derivation
    public static double standardDeviation(ArrayList<Double> list){
    	
    	double sd=0;
    	for(double value: list){
    		sd+=Math.pow((value-average(list)),2);
    	}
    	
    	return Math.sqrt(sd/list.size());
    }
    

}
