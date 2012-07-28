package com.recsys.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		return this.dataMatrix;
	}	
	
	// Top-K neighbor, threashold, notRated: value for unrated items
	public static final int K=2;
	public static final double THREASHOLD=5;
	public static final int notRated=0;
	
	@Override
	public List<Recommendation> recommend(User activeUser) {
		
		
		// similarity Map
    	Map<User,Double> simMap = new HashMap<User,Double>();
    	// estimation Map
    	Map<Item,Double> estimMap = new HashMap<Item,Double>();
    	// user list array
    	ArrayList<User> userList = new ArrayList<User>();
    	// RecommendationList
    	List<Recommendation> recommendationList = new ArrayList<Recommendation>();
    	//Call simPearson method
		simMap=simPearson(activeUser);
		//Call neighborhood method
		userList=neighborhood(simMap,activeUser);
		//Call estimation method
		estimMap=estimation(activeUser,userList);
		
		//Recommendation
	
    	Set<Item> keys= estimMap.keySet();
    	
    	//looking for items with higher rating
	
	    for(Item key:keys){  
	    	if(estimMap.get(key)>=THREASHOLD/2){
		    	  
		    	  recommendationList.add(new Recommendation(this.items.get(items.indexOf(key)),estimMap.get(key)));
		    	  
		      }
	    }
	    for(int col=0;col<this.dataMatrix.getColumnsNumber();col++){
	   		if(this.dataMatrix.get(this.users.indexOf(activeUser), col)>=THREASHOLD/2){
	    		recommendationList.add(new Recommendation(this.items.get(col),this.dataMatrix.get(this.users.indexOf(activeUser), col)));
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
    public Map<User,Double> simPearson(User activeUser){
    
    double simPears=0;
    
    Map<User,Double> simMap = new HashMap<User,Double>();
    ArrayList<Double> user = new ArrayList<Double>();
    ArrayList<Double> activeList = new ArrayList<Double>();
    // looking for common items 
    
    for(int rows=0;rows<this.dataMatrix.getRowsNumber();rows++){
    	
    	if(rows!=this.users.indexOf(activeUser)){
    		
    		for(int cols=0;cols<this.dataMatrix.getColumnsNumber();cols++){
    			if((this.dataMatrix.get(rows,cols)!=notRated)&&(this.dataMatrix.get(this.users.indexOf(activeUser),cols)!=notRated)){
    				
    				activeList.add(this.dataMatrix.get(this.users.indexOf(activeUser),cols));
    				user.add(this.dataMatrix.get(rows,cols));
    			
    		
    			}
    		}
    		//calculates Pearson's correlation coefficient - simPearson(activeUser,allOtherUsers)
    		if(!activeList.isEmpty()&&user.size()>1){
    			
    			for(int nb=0;nb<user.size();nb++){
    		
    				simPears+=(activeList.get(nb)*Mathematics.average(activeList))*(user.get(nb)*Mathematics.average(user));
    		
    			}
    			
    			simPears/=(user.size()-1)*Mathematics.standardDeviation(activeList)*Mathematics.standardDeviation(user);
    			//if similarity is infinite: there is no proof of similarity
    			if(simPears!=Double.NEGATIVE_INFINITY&&simPears!=Double.POSITIVE_INFINITY){
    				//simPears=0;
    			System.out.println("SimPearson User"+this.users.get(rows).getIdUser()+" = "+simPears);
    			simMap.put(this.users.get(rows), simPears);
    			}
    			else{
    				System.out.println("SimPearson User"+this.users.get(rows).getIdUser()+" = 0");
    			}
    		}
    	
    		user.clear();
    		activeList.clear();
    		simPears=0;
    	
    	}
    }
   
    
    return simMap;
    
    }
        
    
    // Looking for Neighborhood
    public ArrayList<User> neighborhood(Map<User,Double> simMap,User activeUser){
    	
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
    	
    	
    	while(this.dataMatrix.get(this.users.indexOf(activeUser),col)!=notRated){
    		
    		col++;
    		
    	}
    	
    	// Sorting the similarity list to get top neighborhood 
    	while(!userList.isEmpty()){
    		
    	double max=simList.get(0);
	    int user=0;
    		    
	    for(int i=1;i<simList.size();i++){
	
    			// comparing simList values
    		if(max<simList.get(i)){
    				max=simList.get(i);
    				user=i;
    		}
    		else 
    		{	//if similarity is equal
    			if(max==simList.get(i)){
    					//comparing dataMatrix value to find the top neighbor
    				if(this.dataMatrix.get(this.users.indexOf(userList.get(user)), col)<this.dataMatrix.get(this.users.indexOf(userList.get(i)), col)){
    					max=simList.get(i);
        				user=i;
    				}
    			}
    			
    		}
    		
    	}
    	
    	neighborList.add(this.users.get(this.users.indexOf(userList.get(user))));
    	
    	userList.remove(user);
    	simList.remove(user);
    	
    	}
    	   	 
    	// Top-k neighborhood
    	
    		while(neighborList.size()>K){
    		
    		neighborList.remove(K);
    		
    		
    		}
    		
	
		return neighborList;
    	
    }
    
    
    // Rating estimation    
    public Map<Item,Double> estimation(User activeUser,ArrayList<User> userList){
    	
    	double estimation;
    	Map<Item,Double> estimMap = new HashMap<Item,Double>();
    	int card=0;
    	
    	if(userList.isEmpty()){
    		System.out.println("No estimation");
    		System.exit(0);
    	}
    	
    	
    	for(int cols=0;cols<this.dataMatrix.getColumnsNumber();cols++){
    		estimation=0;
    		
    		if(this.dataMatrix.get(this.users.indexOf(activeUser),cols)==0){
    			
    			for(User user:userList){
    				if(this.dataMatrix.get(this.users.indexOf(user),cols)!=0){
    					estimation+=this.dataMatrix.get(this.users.indexOf(user),cols);
    					card++;
    				}
    				
    			}
    			if(estimation!=0){
    			estimation/=card;
    			estimMap.put(this.items.get(cols), estimation);
    			}
    			card=0;
	    		
    		}
    		
    		
    	
    	}
    	return estimMap;
    	
    }
    

}
