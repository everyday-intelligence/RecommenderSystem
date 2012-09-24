package com.recsys.matrix;

public class MatrixFactory {

	public static AbstractMatrix createMatrix(int rowsNumber, int columnsNumber){
		//on va lire un fichier de configuretion qui dira quel type de matrice utiliser pour le moment on teste avec la SimpleMatrix
		int matrixType = 0;
		AbstractMatrix m;
		if(matrixType == 0){
			m = createSimpleMatrix(rowsNumber, columnsNumber);
		}else{
			m = createMapMatrix(rowsNumber, columnsNumber);
		}
		for (int i = 0; i < m.getRowsNumber(); i++) {
			for (int j = 0; j < m.getColumnsNumber(); j++) {
				m.set(i, j, 0.0);
			}
		}
		return m;
	}
	
	private static AbstractMatrix createSimpleMatrix(int rowsNumber, int columnsNumber){
		return new SimpleMatrix(rowsNumber, columnsNumber);
	}
	private static AbstractMatrix createMapMatrix(int rowsNumber, int columnsNumber){
		return new MapMatrix(rowsNumber, columnsNumber);
	}
	
}
