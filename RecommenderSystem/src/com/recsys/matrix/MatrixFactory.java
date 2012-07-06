package com.recsys.matrix;

public class MatrixFactory {

	public static AbstractMatrix createMatrix(int rowsNumber, int columnsNumber){
		//on va lire un fichier de configuretion qui dira quel type de matrice utiliser pour le moment on teste avec la SimpleMatrix
		int matrixType = 0;
		if(matrixType == 0){
			return createSimpleMatrix(rowsNumber, columnsNumber);
		}else{
			return  createMapMatrix(rowsNumber, columnsNumber);
		}
	}
	
	private static AbstractMatrix createSimpleMatrix(int rowsNumber, int columnsNumber){
		return new SimpleMatrix(rowsNumber, columnsNumber);
	}
	private static AbstractMatrix createMapMatrix(int rowsNumber, int columnsNumber){
		return new MapMatrix(rowsNumber, columnsNumber);
	}
	
}
