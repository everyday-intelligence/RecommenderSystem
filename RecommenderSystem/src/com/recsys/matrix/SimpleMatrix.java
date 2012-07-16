package com.recsys.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;


public class SimpleMatrix extends AbstractMatrix {

	    protected Double[][] matrix;   // rows-by-columns array

	    // create M-by-N matrix of 0's
	    public SimpleMatrix(int rows,int columns) {
	        super(rows,columns);
	        matrix = new Double[rows][columns];
	    }
	    
	    
	    //matrice random avec les cellules bornées à max
	    public SimpleMatrix(int rows, int columns, int max){
	        super(rows,columns);
	        matrix = new Double[rows][columns];
			for (int i = 0; i < this.getRowsNumber(); i++) {
				for (int j = 0; j < this.getColumnsNumber(); j++) {
					Double v = Math.floor(max*Math.random())%4;
					matrix[i][j] = v;
				}
			}
	    }
	    
	    public Double get(int row, int col){
	  
	    		return matrix[row][col];
	    	
	    }
	    
	    // Insert a value in the matrix
	    public void set(int row,int col,Double vals){
	    	
	    		matrix[row][col]=vals;
	    	    
	    }
	    
	    // calculates average for standard deviation
	    public int average(ArrayList<Double> list){
	    	
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
	    public Map simPearson(int activeUser){
	    
	    double simPears=0;
	    int card=0;	
	    Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	    ArrayList<Double> user = new ArrayList<Double>();
	    ArrayList<Double> activeList = new ArrayList<Double>();
	    // looking for common items 
	    
	    for(int rows=0;rows<this.getRowsNumber();rows++){
	    	
	    	if(rows!=activeUser){
	    		
	    		for(int cols=0;cols<this.getColumnsNumber();cols++){
	    			if((this.get(rows,cols)!=0)&&(this.get(activeUser,cols)!=0)){
	    				
	    				activeList.add(this.get(activeUser,cols));
	    				user.add(this.get(rows,cols));
	    		
	    			}
	    		}
	    		//calculates Pearson's correlation coefficient - simPearson(activeUser,allOtherUsers)
	    		if(!activeList.isEmpty()&&user.size()>1){
	    			
	    			for(int nb=0;nb<user.size();nb++){
	    		
	    				simPears+=(activeList.get(nb)*this.average(activeList))*(user.get(nb)*this.average(user));
	    		
	    			}
	    			
	    			simPears/=(user.size()-1)*this.standardDeviation(activeList)*this.standardDeviation(user);
	    			if(simPears==Float.NEGATIVE_INFINITY){
	    				simPears=0;
	    			}
	    			
	    			
	    			System.out.println("Pearson ="+simPears);
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
	    public ArrayList<Integer> neighborhood(Map<Integer,Double> simMap,int k,int activeUser){
	    	
	    	// Similarity List
	    	ArrayList<Double> simList=new ArrayList<Double>(simMap.values());
	    	// Users List
	    	ArrayList<Integer> userList = new ArrayList<Integer>(simMap.keySet());
	    	
	    	ArrayList<Integer> neighborList = new ArrayList<Integer>();
	    	
	    	int row=0;
	    	
	    	
	    	
	       	
	    	if(simMap.isEmpty()){
	    		
	    		System.out.println("There is no neighbors");
	    		System.exit(0);
	    	}
	    	
	    	
	    	while(this.get(activeUser,row)==0){
	    		row++;
	    	}
	    	
	    	// Sorting the similarity list to get top neighborhood 
	    	while(!userList.isEmpty()){
	    		
	    	double max=simList.get(0);
		    int user=0;
	    		    
		    for(int i=1;i<simList.size();i++){
		    	
	    		//if the similarity is equal to infinity, comparing matrix value
	    		if((max==Double.POSITIVE_INFINITY)&&(simList.get(i)==Double.POSITIVE_INFINITY)){
	    			if(this.get(userList.get(user),row)>this.get(userList.get(i),row)){
	    				user=i;
	    				max=simList.get(user);
	    			}else {
	    				max=simList.get(userList.indexOf(userList.get(i)));
	    				user=userList.indexOf(userList.get(i));
	    			}
	    		}
	    		else
	    			//else comparing simList values
	    		if(max<simList.get(i)){
	    				max=simList.get(i);
	    				user=i;
	    		}
	    		
	    	}
	    	
	    	neighborList.add(userList.get(user));
	    	userList.remove(user);
	    	simList.remove(user);
	    	
	    	}
	    	   	 
	    	
	    	
	    
	    	
	    	// Top-k neighborhood
	    	
	    		while(neighborList.size()>k){
	    		
	    		neighborList.remove(k);
	    		
	    		
	    		}
    	
			return neighborList;
	    	
	    }
	    
	    
	    // Rating estimation    
	    public Map<Integer,Double> estimation(int activeUser,ArrayList<Integer> userList){
	    	
	    	double estimation;
	    	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	    	int card=0;
	    	
	    	if(userList.isEmpty()){
	    		System.out.println("No estimation");
	    		System.exit(0);
	    	}
	    	
	    	
	    	for(int cols=0;cols<this.getColumnsNumber();cols++){
	    		estimation=0;
	    		
	    		if(this.get(activeUser,cols)==0){
	    			
	    			for(int user:userList){
	    				if(this.get(user,cols)!=0){
	    					estimation+=this.get(user,cols);
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
	    public void Recommendation(Map<Integer,Double> estimMap,double THREASHOLD){
	    	
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
	    	SimpleMatrix mat=new SimpleMatrix(10,5);
	    	// similarity Map
	    	Map<Integer,Double> simMap = new HashMap<Integer,Double>();
	    	// estimation Map
	    	Map<Integer,Double> estimMap = new HashMap<Integer,Double>();
	    	// user list array
	    	ArrayList<Integer> userList = new ArrayList<Integer>();
	    	//threashold used to recommend high ranked items
	    	double THREASHOLD=5;
	    	int ACTIVEUSER=0;
	    	
	    	System.out.println("Column: "+mat.getColumnsNumber()+" - Rows: "+mat.getRowsNumber());
	    	//Fill the matrix
	    		//1st column
	 /*   	mat.set(0, 0, 4.0);
	    	mat.set(1, 0, 4.0);
	    	mat.set(2, 0, 3.0);
	    	mat.set(3, 0, 5.0);
	    	mat.set(4, 0, 2.0);
	    		//2nd column
	    	mat.set(0, 1, 2.0);  
	    	mat.set(1, 1, 4.0);
	    	mat.set(2, 1, 3.0);
	    	mat.set(3, 1, 5.0);
	    	mat.set(4, 1, 0.0);
	    		//3rd column
	    	mat.set(0, 2, 0.0);  
	    	mat.set(1, 2, 4.0);
	    	mat.set(2, 2, 3.0);
	    	mat.set(3, 2, 5.0);
	    	mat.set(4, 2, 2.0);
	    	
	   */ 	
	    	
	    	// Fills the matrix with random values
	    	
	    	for (int i = 0; i < mat.getRowsNumber(); i++) {
				for (int j = 0; j < mat.getColumnsNumber(); j++) {
					Double v = Math.floor(6*Math.random());
						mat.set(i, j, v);
					}
				}
			
	    	
	    	
	    	// Print the matrix
	    	System.out.println("Matrix:");
	    		for(int rows=0;rows<mat.getRowsNumber();rows++){
	    			for(int cols=0;cols<mat.getColumnsNumber();cols++){
	    		    System.out.print("["+cols+","+rows+"]");	
	    			System.out.print(mat.get(rows,cols)+" ");
	    		}
	    		System.out.println();
	    	}
	    	
	    	// calculates Pearson correlation coefficient	
	   	    simMap=mat.simPearson(ACTIVEUSER);
	   	    
	   	    //print similarity map
	    	System.out.println("Simularity map content");
	    		    		
	    	System.out.println(simMap);
	    	
	    	
	    	//looking for neighborhood
	    	userList=mat.neighborhood(simMap,2,ACTIVEUSER);
	    	System.out.println("Neighborhood list");
	    	
	    	for(int number=0;number<userList.size();number++){
		    	
	    		System.out.println("user "+userList.get(number));
	    		
	    	}
	    	
	    	
	    	
	    	
	    	//calculate estimated ratings for unrated items
	    	System.out.println("Rating estimation");
	    	
	    	estimMap=mat.estimation(ACTIVEUSER, userList);
	    	
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

	
