package com.recsys.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AttributeValue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long AttributeValueId;
	@ManyToOne
	private Attribute attribute;
	private String value;
	
	
	
	public AttributeValue() {

	}

	public AttributeValue(Attribute attribute, String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public long getAttributeValueId() {
		return AttributeValueId;
	}
	public void setAttributeValueId(long attributeValueId) {
		AttributeValueId = attributeValueId;
	}

	@Override
	public String toString() {
		return attribute +" = "+ value;
	}

}
