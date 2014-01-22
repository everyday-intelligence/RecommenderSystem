package com.recsys.matrix;

import java.io.Serializable;
import java.util.Arrays;



public class VectorMatrix extends AbstractMatrix implements Serializable{

	    protected double[] matrix;   // rows-by-columns array

	    // create M-by-N matrix of 0's
	    public VectorMatrix(int rows,int columns) {
	        super(rows,columns);
	        matrix = new double[rows*columns];
	        for(int i=0;i<getColumnsNumber()*getRowsNumber();i++){
				matrix[i]=0d;
			}
	    }
	    
	    
	    //matrice random avec les cellules born�es � max
	    public VectorMatrix(int rows, int columns, int max){
	        super(rows,columns);
	        matrix = new double[rows*columns];
			for (int i = 0; i < this.getRowsNumber(); i++) {
				for (int j = 0; j < this.getColumnsNumber(); j++) {
					double v = Math.floor(max*Math.random())%4;
					set(i,j,v);
				}
			}
	    }
	    
	    public double get(int row, int col){
	    	return matrix[row*columnsNumber+col];	
	    }
	    
	    // Insert a value in the matrix
	    public void set(int row,int col,double vals){
    		matrix[row*columnsNumber+col]=vals;
	    }
	    
	    
		@Override
		public AbstractVector getRow(int rowNumber) {
			return new SimpleVector(Arrays.copyOfRange(matrix, rowNumber*columnsNumber, rowNumber*(columnsNumber+1)));
		}

		@Override
		public AbstractVector getColumn(int colNumber) {
			double[] col = new double[rowsNumber];
			for(int i=0;i<rowsNumber;i++){
				col[i] = get(i, colNumber);
			}
			return new SimpleVector(col);
		}


		@Override
		public double get(long rowLabel, long colLabel) {
			return get((int)rowLabel,(int)colLabel);
		}


		@Override
		public void set(long rowLabel, long colLabel, double vals) {
			set((int)rowLabel,(int)colLabel,vals);
		}


		@Override
		public AbstractVector getRow(long rowLabel) {
			return getRow((int)rowLabel);
		}


		@Override
		public AbstractVector getColumn(long colLabel) {
			return getColumn((int)colLabel);

		}
		
	
}

	
