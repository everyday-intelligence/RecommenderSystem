package com.recsys.DomainDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.recsys.Domain.Attribute;
import com.recsys.Domain.AttributeType;
import com.recsys.Domain.AttributeValue;
import com.recsys.Domain.User;

public class MovieLensUserDataParser {
	public static final String[] attributesNames = new String[]{"user id","age","gender","occupation","zip code"};
	public static final  boolean[] isComparable = new boolean[]{false,true,true,true,true};
	public static final  AttributeType[] attributesTypes = new AttributeType[]{AttributeType.STRING,AttributeType.NUMERICAL,AttributeType.CATEGORICAL,AttributeType.CATEGORICAL,AttributeType.CATEGORICAL};
	
	public static User parseUserData(String ligne){
		StringTokenizer st = new StringTokenizer(ligne,"|");
		int i=0;
		User user = new User();
		String nextToken = st.nextToken();
		user.setIdUser(Long.parseLong(nextToken));
		Attribute att = new Attribute();
		att.setAttributeName(attributesNames[i]);
		att.setAttributeType(attributesTypes[i]);
		att.setComparable(isComparable[i]);
		AttributeValue attValue = new AttributeValue(att,nextToken);
		user.addAttributeValue(attValue);
		i++;
		while(st.hasMoreTokens()){
			att = new Attribute();
			att.setAttributeName(attributesNames[i]);
			att.setAttributeType(attributesTypes[i]);
			att.setComparable(isComparable[i]);
			attValue = new AttributeValue(att,st.nextToken());
			user.addAttributeValue(attValue);
			//System.out.println(attValue);
			i++;
			
		}
		//System.out.println(i);

		return user;
		
	}

}
