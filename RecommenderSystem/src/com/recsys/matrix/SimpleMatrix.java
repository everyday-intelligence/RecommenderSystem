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
	    	
	    		try {
					matrix[row][col]=vals;
				} catch (Exception e) {
					System.out.println("rating matrix : "+getRowsNumber()+"x"+getColumnsNumber());
					System.out.println("inserting v="+vals+" in "+row+"-"+col);
					e.printStackTrace();
				}
	    	    
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

	
