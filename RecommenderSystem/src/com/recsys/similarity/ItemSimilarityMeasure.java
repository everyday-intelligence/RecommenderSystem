package com.recsys.similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.recsys.Domain.AttributeValue;
import com.recsys.Domain.ValueAttributeNameChecker;
import com.recsys.utils.PredicateUtils;


public class ItemSimilarityMeasure<AttributeValue> extends AttributeSimilarityMeasure<com.recsys.Domain.AttributeValue>{

	private NumbersSimilarityMeasure<Double> nsm;
	
	

	public ItemSimilarityMeasure(NumbersSimilarityMeasure<Double> nsm) {
		super();
		this.nsm = nsm;
	}

	@Override
	public Double measureSimilarity(List<com.recsys.Domain.AttributeValue> valuesItem1, List<com.recsys.Domain.AttributeValue> valuesItem2) {
		// on ne suppose pas que tout les items sont décrits par les memes attributs
		List<Double> CommonAttribsSimilarity = new ArrayList<Double>();

		for(com.recsys.Domain.AttributeValue val1:valuesItem1){
			if(val1.getAttribute().isComparable()){
				List<com.recsys.Domain.AttributeValue> othersValueSameAttrib = (List<com.recsys.Domain.AttributeValue>) PredicateUtils.findAll(valuesItem2, new ValueAttributeNameChecker(val1));
				if(!othersValueSameAttrib.isEmpty()){//attrib exists in itm1 and in itm2
					com.recsys.Domain.AttributeValue otherAttributeValue = othersValueSameAttrib.get(0);
				}
			}
			
		}
		return null;
	}

	@Override
	public Boolean isSimilarity() {
		return nsm.isSimilarity();
	}

}
