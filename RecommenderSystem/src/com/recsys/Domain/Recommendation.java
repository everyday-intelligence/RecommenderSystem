package com.recsys.Domain;


public class Recommendation implements Comparable {

	private Item recommendedItem;//on va voir si item contien juste la référence ou l'identifiant de l'item ou tout ses attributs.
	private Double recommendationValue;//la valeur prédite par l'algo de recommandation (probabilité, note prédite, etc...)
	public Recommendation(Item recommendedItem, Double recommendationValue) {
		super();
		this.recommendedItem = recommendedItem;
		this.recommendationValue = recommendationValue;
	}
	
	public Item getRecommendedItem() {
		return recommendedItem;
	}

	public Double getRecommendationValue() {
		return recommendationValue;
	}

	@Override
	public int compareTo(Object o) {//sert à comparer deux recommandations (laquelle est meilleur)
		Recommendation r = (Recommendation)o;
		//return (int) (this.recommendationValue - r.getRecommendationValue());
		return (int) (this.getRecommendedItem().getIdItem() - r.getRecommendedItem().getIdItem());
	}

	@Override
	public String toString() {
		return "Recommendation ["
				+ (recommendedItem != null ? "recommendedItem="
						+ recommendedItem : "")
				+ (recommendationValue != null ? "recommendationValue="
						+ recommendationValue : "") + "]\n";
	}
	
	
}
