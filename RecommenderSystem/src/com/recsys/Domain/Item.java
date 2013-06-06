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
	private double category; 
	private double[] categoriesMemberships;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idItem ^ (idItem >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (idItem != other.idItem)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [idItem="
				+ idItem
				+ ", "
				+ (attributesValues != null ? "attributesValues="
						+ attributesValues : "") + "]";
	}

	public double getCategory() {
		return category;
	}

	public void setCategory(double category) {
		this.category = category;
	}

	public double[] getCategoriesMemberships() {
		return categoriesMemberships;
	}

	public void setCategoriesMemberships(double[] categoriesMemberships) {
		this.categoriesMemberships = categoriesMemberships;
	}

	
	
}
