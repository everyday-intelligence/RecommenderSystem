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
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else{
			MatrixCoordinates cell = new MatrixCoordinates(row, col);
			Double v = matrix.get(cell);
			if(v==null){
				return new Double(0);// si on n'a pas la valeur �a veut dire que c'est un z�ro qu"on n'a pas sauvegard�.
			}else{
				return v;
			}
			
		}
	}

	@Override
	public void set(int row, int col, Double val) {
		if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else if(val != 0){//si c'est un zero on l'ignore
			//System.out.println("insert "+val);
			matrix.put(new MatrixCoordinates(row, col),val);			
		}
	}
	public int getSize(){
		return matrix.size();
	}
	
}