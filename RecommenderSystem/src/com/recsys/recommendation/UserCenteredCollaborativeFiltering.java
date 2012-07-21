package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.recsys.Domain.Item;
import com.recsys.Domain.User;
import com.recsys.matrix.AbstractMatrix;
import com.recsys.matrix.MatrixFactory;

public class UserCenteredCollaborativeFiltering implements RecommendationStrategy  {


	private List<User> users = new ArrayList<User>();
	private List<Item> items = new ArrayList<Item>();
	private AbstractMatrix dataMatrix;
	
	public UserCenteredCollaborativeFiltering(List<User> users, List<Item> items) {
		super();
		this.users = users;
		this.items = items;
		dataMatrix = MatrixFactory.createMatrix(users.size(), items.size());
		//remplir la matrice avec les historiques des utilisateurs
	}

	
	public AbstractMatrix getDataMatrix(){
		return dataMatrix;
	}
	
	
	@Override
	public List<Recommendation> recommend(User activeUser) {
		int K=3;
		double THREASHOLD=5;
		// similarity Map
    	Map<User,Double> simMap = new HashMap<User,Double>();
    	// estimation Map
    	Map<Item,Double> estimMap = new HashMap<Item,Double>();
    	// user list array
    	ArrayList<User> userList = new ArrayList<User>();
    	// RecommendationList
    	List<Recommendation> recommendationList = new ArrayList<Recommendation>();
    	//Call simPearson method
		simMap=simPearson(activeUser.getIdUser());
		//Call neighborhood method
		userList=neighborhood(simMap,K,activeUser.getIdUser());
		//Call estimation method
		estimMap=estimation(activeUser.getIdUser(),userList);
		
		//Recommendation
		Iterator keys = estimMap.keySet().iterator();
    	
    	
    	//looking for items with higher rating
	    while (keys.hasNext()) {
	      int key = Integer.parseInt(keys.next().toString());
	      
	      if(estimMap.get(key)>=THREASHOLD/2){
	    	  
	    	  System.out.println("item: "+key + " - rating: " + estimMap.get(key));  
	    	  recommendationList.add(new Recommendation(this.items.get(key),estimMap.get(key)));
	    	  
	      }
	      
	      
	    }
		// créer le voisinage
		//estimer
		//prédire les notes
		//retourner toutes les recommandations (tout les produits notés)
		return recommendationList;
	}
	
	//ici les méthodes de recherche de voisinage, estimation, ...... .....
	
	
    
    	    
    // Similarity: Pearson's correlation coefficient 
    public Map<User,Double> simPearson(long activeUserId){
    
    double simPears=0;
    int card=0;	
    Map<User,Double> simMap = new HashMap<User,Double>();
    ArrayList<Double> user = new ArrayList<Double>();
    ArrayList<Double> activeList = new ArrayList<Double>();
    // looking for common items 
    
    for(int rows=0;rows<this.dataMatrix.getRowsNumber();rows++){
    	
    	if(rows!=this.users.indexOf(activeUserId)){
    		
    		for(int cols=0;cols<this.dataMatrix.getColumnsNumber();cols++){
    			if((this.dataMatrix.get(rows,cols)!=0)&&(this.dataMatrix.get(this.users.indexOf(activeUserId),cols)!=0)){
    				
    				activeList.add(this.dataMatrix.get(this.users.indexOf(activeUserId),cols));
    				user.add(this.dataMatrix.get(rows,cols));
    			
    		
    			}
    		}
    		//calculates Pearson's correlation coefficient - simPearson(activeUser,allOtherUsers)
    		if(!activeList.isEmpty()&&user.size()>1){
    			
    			for(int nb=0;nb<user.size();nb++){
    		
    				simPears+=(activeList.get(nb)*Mathematics.average(activeList))*(user.get(nb)*Mathematics.average(user));
    		
    			}
    			
    			simPears/=(user.size()-1)*Mathematics.standardDeviation(activeList)*Mathematics.standardDeviation(user);
    			if(simPears==Double.NEGATIVE_INFINITY){
    				simPears=0;
    			}
    			
    			
    			System.out.println("Pearson ="+simPears);
    			simMap.put(this.users.get(rows), simPears);
    	
    		}
    	
    		user.clear();
    		activeList.clear();
    		simPears=0;
    	
    	}
    }
   
    
    return simMap;
    
    }
        
    
    // Looking for Neighborhood
    public ArrayList<User> neighborhood(Map<User,Double> simMap,int k,long activeUserId){
    	
    	// Similarity List
    	ArrayList<Double> simList=new ArrayList<Double>(simMap.values());
    	// Users List
    	ArrayList<User> userList = new ArrayList<User>(simMap.keySet());
    	
    	ArrayList<User> neighborList = new ArrayList<User>();
    	
    	int col=0;
    	
    	
    	
       	
    	if(simMap.isEmpty()){
    		
    		System.out.println("There is no neighbors");
    		System.exit(0);
    	}
    	
    	
    	while(this.dataMatrix.get(this.users.indexOf(activeUserId),col)==0){
    		col++;
    	}
    	
    	// Sorting the similarity list to get top neighborhood 
    	while(!userList.isEmpty()){
    		
    	double max=simList.get(0);
	    int user=0;
    		    
	    for(int i=1;i<simList.size();i++){
	    	
    		//if the similarity is equal to infinity, comparing matrix value
    		if((max==Double.POSITIVE_INFINITY)&&(simList.get(i)==Double.POSITIVE_INFINITY)){
    			if(this.dataMatrix.get(this.users.indexOf(userList.get(user).getIdUser()),col)>this.dataMatrix.get(this.users.indexOf(userList.get(i).getIdUser()),col)){
    				user=i;
    				max=simList.get(user);
    			}else {
    				max=simList.get(userList.indexOf(userList.get(i)));
    				user=userList.indexOf(userList.get(i));
    			}
    		}
    		else
    			//else comparing simList values
    		if(max<simList.get(i)){
    				max=simList.get(i);
    				user=i;
    		}
    		
    	}
    	
    	neighborList.add(this.users.get(this.users.indexOf(userList.get(user).getIdUser())));
    	userList.remove(user);
    	simList.remove(user);
    	
    	}
    	   	 
    	
    	
    
    	
    	// Top-k neighborhood
    	
    		while(neighborList.size()>k){
    		
    		neighborList.remove(k);
    		
    		
    		}
	
		return neighborList;
    	
    }
    
    
    // Rating estimation    
    public Map<Item,Double> estimation(long activeUserId,ArrayList<User> userList){
    	
    	double estimation;
    	Map<Item,Double> estimMap = new HashMap<Item,Double>();
    	int card=0;
    	
    	if(userList.isEmpty()){
    		System.out.println("No estimation");
    		System.exit(0);
    	}
    	
    	
    	for(int cols=0;cols<this.dataMatrix.getColumnsNumber();cols++){
    		estimation=0;
    		
    		if(this.dataMatrix.get(this.users.indexOf(activeUserId),cols)==0){
    			
    			for(User user:userList){
    				if(this.dataMatrix.get(this.users.indexOf(user.getIdUser()),cols)!=0){
    					estimation+=this.dataMatrix.get(this.users.indexOf(user.getIdUser()),cols);
    					card++;
    				}
    				//System.out.println(this.get(cols,user));
    			}
    			if(estimation!=0){
    			estimation/=card;
    			estimMap.put(this.items.get(cols), estimation);
    			}
    			card=0;
	    		//System.out.println("item :"+cols+" - estimation: "+estimation);	
    		}
    		
    		
    	
    	}
    	return estimMap;
    	
    }
    
  /*  //Recommending items
    public void Recommendation(Map<Integer,Double> estimMap,double THREASHOLD){
    	
    	Iterator keys = estimMap.keySet().iterator();
    	
    	
    	//looking for items with higher rating
	    while (keys.hasNext()) {
	      int key = Integer.parseInt(keys.next().toString());
	      
	      if(estimMap.get(key)>=THREASHOLD/2){
	    	  
	    	  System.out.println("item: "+key + " - rating: " + estimMap.get(key));  
	      }
	      
	      
	    }
    	
    }
    */
	
	
	

}
