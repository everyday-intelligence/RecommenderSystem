package com.recsys.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public /*abstract*/ class Item implements Serializable{
	@Id
	private long idItem;

	public Item(){}
	
	public Item(long idItem) {
		super();
		this.idItem = idItem;
	}



	public long getIdItem() {
		return idItem;
	}



	@Override
	public boolean equals(Object o) {
		
		return (this.idItem==((Item)o).idItem);
	}

	@Override
	public String toString() {
		return "Item [idItem=" + idItem + "]";
	}
	
	
}
