package com.recsys.matrix;

import java.util.HashMap;
import java.util.Map;

public class MapMatrix extends AbstractMatrix {


	Map<MatrixCoordinates,Double> matrix = new HashMap<MatrixCoordinates,Double>();

	public MapMatrix(int columnsNumber, int rowsNumber) {
		super(columnsNumber, rowsNumber);
	}

	@Override
	protected Double get(int row, int col) {
		if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
		}else{
			Double v = matrix.get(new MatrixCoordinates(row, col));
			if(v==null){
				return new Double(0);// si on n'a pas la valeur ça veut dire que c'est un zéro qu"on n'a pas sauvegardé.
			}else{
				return v;
			}
			
		}
	}

	@Override
	protected void set(int row, int col, Double val) {
		if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonnées hors de la matrice"));
		}else if(val != 0){//si c'est un zero on l'ignore
			
			matrix.put(new MatrixCoordinates(row, col),val);			
		}
	}
}
