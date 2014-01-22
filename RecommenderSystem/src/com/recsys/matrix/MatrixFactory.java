package com.recsys.matrix;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;

public class MatrixFactory {

	public static final int INDEXED_SIMPLE_MATRIX = 0;
	public static final int INDEXED_JBLAS_MATRIX = 1;
	public static final int INDEXED_VECTOR_MATRIX = 2;
	
	
	public static final int matrixType = 0;


	public static AbstractMatrix createMatrix(List<User> users, List<Item> items){
		//on va lire un fichier de configuretion qui dira quel type de matrice utiliser pour le moment on teste avec la SimpleMatrix
		List<Long> usersLabels = new ArrayList<Long>();
		List<Long> itemsLabels = new ArrayList<Long>();
		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		
		if(matrixType==INDEXED_SIMPLE_MATRIX){
			return new IndexedSimpleMatrix(usersLabels,itemsLabels);
		}else if(matrixType==INDEXED_JBLAS_MATRIX){
			return new IndexedJBlasMatrix(usersLabels,itemsLabels);
		}else if(matrixType==INDEXED_VECTOR_MATRIX){
			return new IndexedVectorMatrix(usersLabels,itemsLabels);
		}else{
			return null;
		}
	}

	/*
	public static AbstractMatrix createMatrix(int nbR,int nbC){
		//on va lire un fichier de configuretion qui dira quel type de matrice utiliser pour le moment on teste avec la SimpleMatrix
		int matrixType = 1;
		if(matrixType==SIMPLE_INDEXED_MATRIX){
			return createIndexedSimpleMatrix(nbR,nbC);
		}else if(matrixType==INDEXED_JBLAS_MATRIX){
			return createIndexedJBlasMatrix(nbR,nbC);
		}else if(matrixType==Vector_MATRIX){
			return createSimpleMatrix(nbR,nbC);
		}else if(matrixType==EJML_MATRIX){
			return createEJMLMatrix(nbR,nbC);
		}else{
			return createVectorMatrix(nbR,nbC);			
		}
	}
	*/
	
	
	public static AbstractMatrix createItemsMatrix(List<Item> items) {
		List<Long> itemsLabels = new ArrayList<Long>();

		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		if(matrixType==INDEXED_SIMPLE_MATRIX){
			return new IndexedSimpleMatrix(itemsLabels,itemsLabels);
		}else if(matrixType==INDEXED_JBLAS_MATRIX){
			return new IndexedJBlasMatrix(itemsLabels,itemsLabels);
		}else if(matrixType==INDEXED_VECTOR_MATRIX){
			return new IndexedVectorMatrix(itemsLabels,itemsLabels);
		}else{
			return null;
		}
	}
	public static AbstractMatrix createUsersMatrix(List<User> users) {
		List<Long> usersLabels = new ArrayList<Long>();

		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		if(matrixType==INDEXED_SIMPLE_MATRIX){
			return new IndexedSimpleMatrix(usersLabels,usersLabels);
		}else if(matrixType==INDEXED_JBLAS_MATRIX){
			return new IndexedJBlasMatrix(usersLabels,usersLabels);
		}else if(matrixType==INDEXED_VECTOR_MATRIX){
			return new IndexedVectorMatrix(usersLabels,usersLabels);
		}else{
			return null;
		}
	}
	private static AbstractMatrix createEJMLMatrix(int nbR, int nbC) {
		return new EJMLMatrix(nbR, nbC);
	}

	private static AbstractMatrix createVectorMatrix(int nbR, int nbC) {
		return new VectorMatrix(nbR,nbC);
	}

	private static AbstractMatrix createSimpleMatrix(int nbR,int nbC){
		return new SimpleMatrix(nbR,nbC);
	}
	
	public static AbstractMatrix createIndexedSimpleMatrix(int nbR,int nbC) {
		return new IndexedSimpleMatrix(nbR,nbC);
	}
	public static AbstractMatrix createIndexedJBlasMatrix(int nbR,int nbC) {
		return new IndexedJBlasMatrix(nbR,nbC);
	}
}
