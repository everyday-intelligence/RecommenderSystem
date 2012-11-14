package com.recsys.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public /*abstract*/ class Item implements Serializable{
	@Id
	private long idItem;
	private List<AttributeValue> attributesValues;

	public Item(){}
	
	public Item(long idItem) {
		super();
		this.idItem = idItem;
	}



	public long getIdItem() {
		return idItem;
	}





	public void setIdItem(long idItem) {
		this.idItem = idItem;
	}

	public void setAttributesValues(List<AttributeValue> attributesValues) {
		this.attributesValues = attributesValues;
	}

	public List<AttributeValue> getAttributesValues() {
		return attributesValues;
	}

	public void addAttributeValue(AttributeValue attributeValue) {
		if(this.attributesValues == null){
			this.attributesValues = new ArrayList<AttributeValue>();
		}
		this.attributesValues.add(attributeValue);
	}
	
	@Override
	public boolean equals(Object o) {
		
		return (this.idItem==((Item)o).getIdItem());
	}

	@Override
	public String toString() {
		return "Item [idItem="
				+ idItem
				+ ", "
				+ (attributesValues != null ? "attributesValues="
						+ attributesValues : "") + "]";
	}

	
	
}
