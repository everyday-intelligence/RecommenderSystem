package com.recsys.matrix;

import java.util.HashMap;
import java.util.Map;

public class MapMatrix extends AbstractMatrix {


	Map<MatrixCoordinates,Double> matrix = new HashMap<MatrixCoordinates,Double>();

	public MapMatrix(int columnsNumber, int rowsNumber) {
		super(columnsNumber, rowsNumber);
	}

	@Override
	public  Double get(int row, int col) {
		if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
		}else{
			MatrixCoordinates cell = new MatrixCoordinates(row, col);
			Double v = matrix.get(cell);
			if(v==null){
				return new Double(0);// si on n'a pas la valeur ça veut dire que c'est un zéro qu"on n'a pas sauvegardé.
			}else{
				return v;
			}
			
		}
	}

	@Override
	public void set(int row, int col, Double val) {
		if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
		}else if(val != 0){//si c'est un zero on l'ignore
			//System.out.println("insert "+val);
			matrix.put(new MatrixCoordinates(row, col),val);			
		}
	}
	
	
	public int getRealSize(){
		return matrix.size();
	}

	 

	@Override
	public AbstractVector getRow(int rowNumber) {
		MapVector v = new MapVector(this.rowsNumber);
	    for (MatrixCoordinates mc : this.matrix.keySet()) {
	         if (mc.getRow() == rowNumber)
	             v.set(mc.getColumn(), matrix.get(mc));
	    }
	    return v;
	}

	@Override
	public AbstractVector getColumn(int colNumber) {
		MapVector v = new MapVector(this.rowsNumber);
	    for (MatrixCoordinates mc : this.matrix.keySet()) {
	         if (mc.getColumn() == colNumber)
	             v.set(mc.getRow(), matrix.get(mc));
	    }
	    return v;
	}
	
}
