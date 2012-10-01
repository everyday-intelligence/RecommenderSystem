package com.recsys.matrix;



public class SimpleMatrix extends AbstractMatrix {

	    protected Double[][] matrix;   // rows-by-columns array

	    // create M-by-N matrix of 0's
	    public SimpleMatrix(int rows,int columns) {
	        super(rows,columns);
	        matrix = new Double[rows][columns];
	        for(int i=0;i<getRowsNumber();i++){
				for(int j=0;j<getColumnsNumber();j++){
					set(i,j,0d);
				}
			}
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
	    	if(matrix[row][col]==null){System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!"+row+" , "+col);}
	    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
				throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
			}else{
	    		return matrix[row][col];
			}
	    }
	    
	    // Insert a value in the matrix
	    public void set(int row,int col,Double vals){
	    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
				throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
			}else{
	    		matrix[row][col]=vals;
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

	
