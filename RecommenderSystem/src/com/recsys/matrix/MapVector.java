package com.recsys.matrix;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapVector extends AbstractVector{

	public MapVector(int length) {
		super(length);
		// TODO Auto-generated constructor stub
	}
	private Map<Integer,Double> vector = new HashMap<Integer,Double>();
	
	@Override
	public  Double get(int index) {
		if(index<0){
			throw(new IndexOutOfBoundsException("coordonnées hors du vecteur"));
		}else{
			Double v = vector.get(index);
			if(v==null){
				return new Double(0);// si on n'a pas la valeur ça veut dire que c'est un zéro qu"on n'a pas sauvegardé.
			}else{
				return v;
			}
			
		}
	}

	@Override
	public void set(int index, Double val) {
		if(index<0){
			throw(new IndexOutOfBoundsException("coordonnées hors du vecteur"));
		}else if(val != 0){//si c'est un zero on l'ignore
			//System.out.println("insert "+val);
			vector.put(index,val);			
		}
	}
	
	public int getRealSize(){
		return vector.size();
	}
	public int size() {
		return Collections.max(vector.keySet());
	}
}
