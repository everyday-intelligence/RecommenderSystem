package com.recsys.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "Product")
public /*abstract*/ class Item {
	@Id
	private long idItem;

	public Item(long idItem) {
		super();
		this.idItem = idItem;
	}



	public long getIdItem() {
		return idItem;
	}



	@Override
	public String toString() {
		return "Item [idItem=" + idItem + "]";
	}
	
	
}
