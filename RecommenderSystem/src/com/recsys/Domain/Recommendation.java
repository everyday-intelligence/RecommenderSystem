package com.recsys.Domain;


public class Recommendation implements Comparable {

	private Item recommendedItem;//on va voir si item contien juste la r�f�rence ou l'identifiant de l'item ou tout ses attributs.
	private Double recommendationValue;//la valeur pr�dite par l'algo de recommandation (probabilit�, note pr�dite, etc...)
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
	public int compareTo(Object o) {//sert � comparer deux recommandations (laquelle est meilleur)
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
