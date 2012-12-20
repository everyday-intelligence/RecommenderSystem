package com.recsys.DomainDAO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.recsys.Domain.AttributeType;
import com.recsys.Domain.AttributeValue;
import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public class MovieLens100KDataReader{

	// Read the users' Id from the file
	public static List<User> findUsersFile(String fichier) {
		List<User> userList = new ArrayList<User>();
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			while ((ligne = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(ligne, "|");
				userList.add(new User(Long.parseLong(st.nextToken())));

			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return userList;
	}

	public static List<Item> findItemsFile(String fichier) {
		List<Item> itemList = new ArrayList<Item>();
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			while ((ligne = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(ligne, "|");
				//itemList.add(new Item(Long.parseLong(st.nextToken())));
				Item itm = MovieLensItemDataParser.parseItemData(ligne);
				itemList.add(itm);

			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return itemList;
	}
	
	public static Instances fromItemsToWekaDataset(String fichier) {
		List<Item> items = findItemsFile(fichier);
		List<Instance> instancesList = new ArrayList<Instance>();
		int numAttributes = MovieLensItemDataParser.attributesNames.length;
		Map<String, ArrayList<String>> categoricalVariablesValues = new HashMap<String,ArrayList<String>>();
		for(Item itm:items){
			int i=0;
			double attVals[] = new double[numAttributes]; 
			for(AttributeValue attVal:itm.getAttributesValues()){
				//System.out.println(itm.getAttributesValues().size());

				switch(attVal.getAttribute().getAttributeType()){
					case NUMERICAL :
						attVals[i]=Double.parseDouble(attVal.getValue());
						break;
					case BOOLEAN:
					case CATEGORICAL:
					case STRING:
						String v = attVal.getValue();
						if(!categoricalVariablesValues.containsKey(attVal.getAttribute().getAttributeName())){
							categoricalVariablesValues.put(attVal.getAttribute().getAttributeName(), new ArrayList<String>());
						}
						if(!categoricalVariablesValues.get(attVal.getAttribute().getAttributeName()).contains(v)){
							categoricalVariablesValues.get(attVal.getAttribute().getAttributeName()).add(v);
						}
						attVals[i]=categoricalVariablesValues.get(attVal.getAttribute().getAttributeName()).indexOf(v);
						//System.out.println(categoricalVariablesValues.values());
						break;
					case DATE:
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					try {
						attVals[i]=sdf.parse(attVal.getValue()).getTime();
					} catch (ParseException e) {
						attVals[i]= 0;
						System.out.println(e);
					}
						break;
					default:
						
				}
				i++;
				
			}
			instancesList.add(new Instance(1,attVals));
		}
		//System.out.println(categoricalVariablesValues);
		FastVector attributes = new FastVector();
		for(int i=0;i< MovieLensItemDataParser.attributesNames.length;i++){
			switch(MovieLensItemDataParser.attributesTypes[i]){
				case NUMERICAL :
					attributes.addElement(new Attribute(MovieLensItemDataParser.attributesNames[i]));
					break;
				case BOOLEAN:
				case CATEGORICAL:
				case STRING:
					FastVector categoricals = new FastVector();
					ArrayList<String> attpossvals = categoricalVariablesValues.get(MovieLensItemDataParser.attributesNames[i]);
					//System.out.println("pos vals de "+MovieLensItemDataParser.attributesNames[i]+" sont "+attpossvals);
					for(String possibleValue:attpossvals){
						categoricals.addElement(possibleValue);
					}
				    attributes.addElement(new Attribute(MovieLensItemDataParser.attributesNames[i], categoricals));
				    break;
				case DATE:
					attributes.addElement(new Attribute(MovieLensItemDataParser.attributesNames[i], "dd-MMM-yyyy"));
					//attributes.addElement(new Attribute(MovieLensItemDataParser.attributesNames[i], (FastVector)null));
					break;
				default:
			}
		}
				
		Instances data = new Instances("MyData", attributes, 0);
		//System.out.println(data.attribute(10));

		for(Instance in:instancesList){
			in.setDataset(data);
			data.add(in);
		}
		//System.out.println(categoricalVariablesValues.keySet());
		//System.out.println(categoricalVariablesValues);

//		Enumeration enumAtt = data.enumerateAttributes();
//		while(enumAtt.hasMoreElements()){
//			Attribute a = (Attribute) enumAtt.nextElement();
//			System.out.println(a);
//		}
		
		return data;
	}
	
	

	public static List<Rating> findRatingsFile(String fichier) {
		List<Rating> ratingList = new ArrayList<Rating>();
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			long userId;
			long itemId;
			double rating;

			while ((ligne = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(ligne,"\t");

				userId = Long.parseLong(st.nextToken());
				itemId = Long.parseLong(st.nextToken());
				rating = Double.parseDouble(st.nextToken());

				ratingList.add(new Rating(rating, new Item(itemId), new User(
						userId)));

			}

			// ratingList.add(new Rating(Long.parseLong(st.nextToken())));

			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return ratingList;

	}

	public static List<Rating> findUserRatings(String fichier, long l) {
		
		 List<Rating> allRatings = findRatingsFile(fichier);
		 List<Rating> userRatings = new ArrayList<Rating>();
		 for(Rating r : allRatings){
			 if(r.getRatingUser().getIdUser() == l){
				 userRatings.add(r);
			 }
		 }
		 return userRatings;

	}

}
