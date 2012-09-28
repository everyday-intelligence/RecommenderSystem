package com.recsys.DomainDAO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public class MoveieLens1MDataReader{

	// Read the users' Id from the file
	public static List<User> findUsersFile(String fichier) {
		List<User> userList = new ArrayList<User>();
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			while ((ligne = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(ligne, "::");
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

				StringTokenizer st = new StringTokenizer(ligne, "::");
				itemList.add(new Item(Long.parseLong(st.nextToken())));

			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return itemList;
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

				StringTokenizer st = new StringTokenizer(ligne,"::");

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
