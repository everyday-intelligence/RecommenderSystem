package com.recsys.matrix;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;

public class MatrixFactory {

	public static final int SIMPLE_INDEXED_MATRIX = 0;
	public static final int SIMPLE_JBLAS_MATRIX = 1;
	

	public static AbstractMatrix createMatrix(List<User> users, List<Item> items){
		//on va lire un fichier de configuretion qui dira quel type de matrice utiliser pour le moment on teste avec la SimpleMatrix
		int matrixType = SIMPLE_INDEXED_MATRIX;
		if(matrixType==SIMPLE_INDEXED_MATRIX){
			return createIndexedSimpleMatrix(users,items);
		}else{
			return createIndexedJBlasMatrix(users,items);
		}
	}
	
	private static AbstractMatrix createSimpleMatrix(int rowsNumber, int columnsNumber){
		return new SimpleMatrix(rowsNumber, columnsNumber);
	}
	
	public static AbstractMatrix createIndexedSimpleMatrix(List<User> users, List<Item> items) {
		List<Long> usersLabels = new ArrayList<Long>();
		List<Long> itemsLabels = new ArrayList<Long>();
		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		return new IndexedSimpleMatrix(usersLabels, itemsLabels);
	}
	public static AbstractMatrix createIndexedJBlasMatrix(List<User> users, List<Item> items) {
		List<Long> usersLabels = new ArrayList<Long>();
		List<Long> itemsLabels = new ArrayList<Long>();
		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		return new IndexedJBlasMatrix(usersLabels, itemsLabels);
	}
	public static AbstractMatrix createItemsMatrix(List<Item> items) {
		List<Long> itemsLabels = new ArrayList<Long>();

		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		return new IndexedSimpleMatrix(itemsLabels, itemsLabels);
	}
	public static AbstractMatrix createUsersMatrix(List<User> users) {
		List<Long> usersLabels = new ArrayList<Long>();

		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		return new IndexedSimpleMatrix(usersLabels, usersLabels);
	}
}
