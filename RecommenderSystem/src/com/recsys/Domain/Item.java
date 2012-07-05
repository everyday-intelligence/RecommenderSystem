package com.recsys.Domain;

public abstract class Item {

	private long idItem;

	public Item(long idItem) {
		super();
		this.idItem = idItem;
	}



	public long getIdItem() {
		return idItem;
	}
	
	
}
