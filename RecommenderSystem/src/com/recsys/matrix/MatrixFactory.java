package com.recsys.matrix;

import java.util.ArrayList;
import java.util.List;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;

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
	public static IndexedSimpleMatrix createMatrix(List<User> users, List<Item> items) {
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
	public static IndexedSimpleMatrix createItemsMatrix(List<Item> items) {
		List<Long> itemsLabels = new ArrayList<Long>();

		for(int i=0;i<items.size();i++){
			itemsLabels.add(items.get(i).getIdItem());
		}
		return new IndexedSimpleMatrix(itemsLabels, itemsLabels);
	}
	public static IndexedSimpleMatrix createUsersMatrix(List<User> users) {
		List<Long> usersLabels = new ArrayList<Long>();

		for(int i=0;i<users.size();i++){
			usersLabels.add(users.get(i).getIdUser());
		}
		return new IndexedSimpleMatrix(usersLabels, usersLabels);
	}
}
