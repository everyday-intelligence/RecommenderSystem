package com.recsys.DomainDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.recsys.Domain.Attribute;
import com.recsys.Domain.AttributeType;
import com.recsys.Domain.AttributeValue;
import com.recsys.Domain.Item;

public class MovieLensItemDataParser {
	
	public static final String[] attributesNames = new String[]{"movie id","movie title","release date","video release date","IMDb URL","unknown","Action","Adventure","Animation","Children's","Comedy","Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller","War","Western"};
	//public static final  boolean[] isAProperty = new boolean[]{false,false,true,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
	public static final  boolean[] isComparable = new boolean[]{false,false,true,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
	public static final  AttributeType[] attributesTypes = new AttributeType[]{AttributeType.STRING,AttributeType.STRING,AttributeType.NUMERICAL,AttributeType.NUMERICAL,AttributeType.STRING,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN,AttributeType.BOOLEAN};
	
	public static Item parseItemData(String ligne){
		ligne = ligne.replaceAll("\\|\\|", "|01-Jan-1970|");
		//System.out.println(ligne);
		StringTokenizer st = new StringTokenizer(ligne,"|");

		int i=0;
		Item item = new Item();
		String nextToken = st.nextToken();
		item.setIdItem(Long.parseLong(nextToken));
		Attribute att = new Attribute();
		att.setAttributeName(attributesNames[i]);
		att.setAttributeType(attributesTypes[i]);
		att.setComparable(isComparable[i]);
		AttributeValue attValue = new AttributeValue(att,nextToken);
		item.addAttributeValue(attValue);
		i++;
		while(st.hasMoreTokens()){
			att = new Attribute();
			att.setAttributeName(attributesNames[i]);
			//att.setAProperty(isAProperty[i]);
			att.setAttributeType(attributesTypes[i]);
			att.setComparable(isComparable[i]);
			if(attributesTypes[i]==AttributeType.NUMERICAL){
				int annee = 0;
				try {
					annee = new SimpleDateFormat("dd-MMM-yyyy").parse(st.nextToken()).getYear();
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				attValue = new AttributeValue(att,annee+"");
			}else{
				attValue = new AttributeValue(att,st.nextToken());
			}
			item.addAttributeValue(attValue);
			//System.out.println(attValue);
			i++;
			
		}
		//System.out.println(i);

		return item;
		
	}

}
