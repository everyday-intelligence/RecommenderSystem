package com.recsys.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;


public class SimpleMatrix extends AbstractMatrix {

	    protected Double[][] matrix;   // rows-by-columns array

	    // create M-by-N matrix of 0's
	    public SimpleMatrix(int columns, int rows) {
	        super(columns,rows);
	        matrix = new Double[columns][rows];
	    }
	    
	    protected Double get(int row, int col){
	  
	    		return matrix[row][col];
	    	
	    }
	    
	    // Insert a value in the matrix
	    protected void set(int row,int col,Double vals){
	    	
	    		matrix[row][col]=vals;
	    	    
	    }
	    
	    // calculates average for standard deviation
	    protected int average(ArrayList<Double> list){
	    	
	    	double avg=0;
	    	for(double value: list){
	    		
	    		avg+=value;
	    		
	    	}
	    	
	    	return (int)avg/list.size();
	    	
	    }
	    
	    
	    protected double standardDeviation(ArrayList<Double> list){
	    	
	    	double sd=0;
	    	for(double value: list){
	    		sd+=Math.pow((value-this.average(list)),2);
	    	}
	    	
	    	return Math.sqrt(sd/list.size());
	    }
	    
	    
	    	    
	    // Similarity: Pearson's correlation coefficient 
	    //Le calcul est � faire, c'est juste pour tester
	    protected Map simPearson(int activeUser){
	    
	    double simPears=0;
	    int card=0;	
	    Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	    ArrayList<Double> user = new ArrayList<Double>();
	    ArrayList<Double> activeList = new ArrayList<Double>();
	    // looking for common items 
	    
	    for(int rows=0;rows<this.getRowsNumber();rows++){
	    	
	    	if(rows!=activeUser){
	    		
	    		for(int cols=0;cols<this.getColumnsNumber();cols++){
	    			if((this.get(cols, rows)!=0)&&(this.get(cols,activeUser)!=0)){
	    				
	    				activeList.add(this.get(cols,activeUser));
	    				user.add(this.get(cols, rows));
	    		
	    			}
	    		}
	    		//calculates Pearson's correlation coefficient - simPearson(activeUser,allOtherUsers)
	    		if(!activeList.isEmpty()){
	    	
	    			for(int nb=0;nb<user.size();nb++){
	    		
	    				simPears+=(activeList.get(nb)*this.average(activeList))*(user.get(nb)*this.average(user));
	    		
	    			}
	    	
	    			simPears/=(user.size()-1)*this.standardDeviation(activeList)*this.standardDeviation(user);
	    	
	    			simMap.put(rows, simPears);
	    	
	    		}
	    	
	    		user.clear();
	    		activeList.clear();
	    		simPears=0;
	    	
	    	}
	    }
	   
	    
	    return simMap;
	    
	    }
	        
	    
	    // Looking for Neighborhood
	    protected ArrayList<Integer> neighborhood(Map<Integer,Double> simMap,int k){
	    	
	    	// Similarity List
	    	ArrayList<Double> simList=new ArrayList<Double>(simMap.values());
	    	// Users List
	    	ArrayList<Integer> userList = new ArrayList<Integer>();
	    	
	    	// Sorting the similarity list to get top neighborhood 	    	
	    	Collections.sort(simList,Collections.reverseOrder());
	    	
	    	
	    	if(simMap.isEmpty()){
	    		
	    		System.out.println("There is no neighbors");
	    		System.exit(0);
	    	}
	    	

	    	// Finding the users with the similarity	    	
	    	for(int i=0;i<simMap.size();i++){
	    		
	    		Iterator keys = simMap.keySet().iterator();
	    		
	    		while (keys.hasNext()) {
	    	    
	    			int key = Integer.parseInt(keys.next().toString());
	    			
	    			if(simMap.get(key).equals(simList.get(i))){
	    				
	    				if(!userList.contains(key)){
	    					
	    					userList.add(key);
	    				}
	    			}
		    		
	    		}
	    	
	    	}
	    	    
	    	
	    	// Top-k neighborhood
	    	for(int comp=k;comp<=userList.size();comp++){
	    		
	    		userList.remove(k);
	    		
	    	}
	    	
	    	    	
			return userList;
	    	
	    }
	    
	    
	    // Rating estimation    
	    protected Map<Integer,Double> estimation(int activeUser,ArrayList<Integer> userList){
	    	
	    	double estimation;
	    	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	    	int card=0;
	    	
	    	if(userList.isEmpty()){
	    		System.out.println("No estimation");
	    		System.exit(0);
	    	}
	    	
	    	
	    	for(int cols=0;cols<this.getColumnsNumber();cols++){
	    		estimation=0;
	    		
	    		if(this.get(cols,activeUser )==0){
	    			
	    			for(int user:userList){
	    				if(this.get(cols,user)!=0){
	    					estimation+=this.get(cols,user);
	    					card++;
	    				}
	    				//System.out.println(this.get(cols,user));
	    			}
	    			if(estimation!=0){
	    			estimation/=card;
	    			estimMap.put(cols, estimation);
	    			}
	    			card=0;
		    		//System.out.println("item :"+cols+" - estimation: "+estimation);	
	    		}
	    		
	    		
	    	
	    	}
	    	return estimMap;
	    	
	    }
	    
	    //Recommending items
	    protected void Recommendation(Map<Integer,Double> estimMap,double THREASHOLD){
	    	
	    	Iterator keys = estimMap.keySet().iterator();
	    	
	    	
	    	//looking for items with higher rating
    	    while (keys.hasNext()) {
    	      int key = Integer.parseInt(keys.next().toString());
    	      
    	      if(estimMap.get(key)>=THREASHOLD/2){
    	    	  System.out.println("item: "+key + " - rating: " + estimMap.get(key));  
    	      }
    	      
    	      
    	    }
	    	
	    }
	    
	    
	    // main 
	    public static void main(String args[]){
	    	
	    	// matrix with 3 columns and 2 rows
	    	SimpleMatrix mat=new SimpleMatrix(3,5);
	    	// similarity Map
	    	Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	    	// estimation Map
	    	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	    	// user list array
	    	ArrayList<Integer> userList = new ArrayList<Integer>();
	    	//threashold used to recommend high ranked items
	    	double THREASHOLD=5;
	    	
	    	System.out.println("Column: "+mat.getColumnsNumber()+" - Rows: "+mat.getRowsNumber());
	    	//Fill the matrix
	    		//1st column
	    	mat.set(0, 0, 4.0);
	    	mat.set(0, 1, 1.0);
	    	mat.set(0, 2, 3.0);
	    	mat.set(0, 3, 5.0);
	    	mat.set(0, 4, 2.0);
	    		//2nd column
	    	mat.set(1, 0, 2.0);  
	    	mat.set(1, 1, 3.0);
	    	mat.set(1, 2, 1.0);
	    	mat.set(1, 3, 5.0);
	    	mat.set(1, 4, 0.0);
	    		//3rd column
	    	mat.set(2, 0, 0.0);  
	    	mat.set(2, 1, 3.0);
	    	mat.set(2, 2, 0.0);
	    	mat.set(2, 3, 3.0);
	    	mat.set(2, 4, 2.0);
	    	
	    	// Print the matrix
	    	System.out.println("Matrix:");
	    		for(int rows=0;rows<mat.getRowsNumber();rows++){
	    			for(int cols=0;cols<mat.getColumnsNumber();cols++){
	    		    System.out.print("["+cols+","+rows+"]");	
	    			System.out.print(mat.get(cols,rows)+" ");
	    		}
	    		System.out.println();
	    	}
	    	
	    	// calculates Pearson correlation coefficient	
	   	    simMap=mat.simPearson(0);
	   	    
	   	    //print similarity map
	    	System.out.println("Simularity map content");
	    	for(int number=0;number<simMap.size();number++){
	    	
	    		System.out.println(simMap.get(number));
	    		
	    	}
	    	
	    	
	    	//looking for neighborhood
	    	userList=mat.neighborhood(simMap,2);
	    	System.out.println("Neighborhood list");
	    	
	    	for(int number=0;number<userList.size();number++){
		    	
	    		System.out.println("user "+userList.get(number));
	    		
	    	}
	    	
	    	//calculate estimated ratings for unrated items
	    	System.out.println("Rating estimation");
	    	
	    	estimMap=mat.estimation(0, userList);
	    	
	    	System.out.println("items :"+estimMap.keySet()+" - estimation :"+estimMap.values());
	    	
	    	//print items recommended by the system
	    	System.out.println("Recommending Items");
	    	
	    	mat.Recommendation(estimMap, THREASHOLD);
	    	
	    }

		@Override
		public AbstractVector getRow(int rowNumber) {
			return new SimpleVector(matrix[rowNumber]);
		}

		@Override
		public AbstractVector getColumn(int colNumber) {
			Double[] v = new Double[this.rowsNumber];
			for(int i=0;i<this.rowsNumber;i++){
				v[i]=matrix[i][colNumber];
			}
			return new SimpleVector(v);
		}
		
	
}

	
