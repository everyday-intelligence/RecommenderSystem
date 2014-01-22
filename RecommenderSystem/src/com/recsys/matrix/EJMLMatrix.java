package com.recsys.matrix;

import java.io.Serializable;

import org.ejml.data.DenseMatrix64F;



public class EJMLMatrix extends AbstractMatrix implements Serializable{

	    protected DenseMatrix64F matrix;   // rows-by-columns array

	    // create M-by-N matrix of 0's
	    public EJMLMatrix(int rows,int columns) {
	        super(rows,columns);
	        matrix = new DenseMatrix64F(rows, columns);
	        for(int i=0;i<getRowsNumber();i++){
				for(int j=0;j<getColumnsNumber();j++){
					set(i,j,0d);
				}
			}
	        
	    }
	    
	    
	    //matrice random avec les cellules born�es � max
	    public EJMLMatrix(int rows, int columns, int max){
	        super(rows,columns);
	        matrix = new DenseMatrix64F(rows, columns);
			for (int i = 0; i < this.getRowsNumber(); i++) {
				for (int j = 0; j < this.getColumnsNumber(); j++) {
					double v = Math.floor(max*Math.random())%4;
					set(i, j, v);
				}
			}
	    }
	    
	    public double get(int row, int col){
	    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
				throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
			}else{
	    		return matrix.get(row,col);
			}
	    }
	    
	    // Insert a value in the matrix
	    public void set(int row,int col,double vals){
	    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
				throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
			}else{
	    		matrix.set(row, col, vals);
			}
	    }
	    
	    
		@Override
		public AbstractVector getRow(int rowNumber) {
			return null;
		}

		@Override
		public AbstractVector getColumn(int colNumber) {
			return null;
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

	
