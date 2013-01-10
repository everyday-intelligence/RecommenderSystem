package com.recsys.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Attribute implements Serializable{
	
	@Id
	private String attributeName;
	@Enumerated(EnumType.STRING)
	private AttributeType attributeType;
	private boolean isComparable;
	private boolean isAProperty;
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public AttributeType getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public boolean isComparable() {
		return isComparable;
	}
	public void setComparable(boolean isComparable) {
		this.isComparable = isComparable;
	}
	public boolean isAProperty() {
		return isAProperty;
	}
	public void setAProperty(boolean isAProperty) {
		this.isAProperty = isAProperty;
	}
	@Override
	public String toString() {
		return attributeName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeName == null) ? 0 : attributeName.hashCode());
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
		Attribute other = (Attribute) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		return true;
	}
	
}
