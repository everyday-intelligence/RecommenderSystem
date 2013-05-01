package com.recsys.Domain;

public class GroupCategoryRating {

	private double group;
	private double category;
	private double rating;
	public GroupCategoryRating(double group, double category,
			double rating) {
		super();
		this.group = group;
		this.category = category;
		this.rating = rating;
	}
	public double getGroup() {
		return group;
	}
	public double getCategory() {
		return category;
	}
	public double getRating() {
		return rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(category);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(group);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rating);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupCategoryRating other = (GroupCategoryRating) obj;
		if (Double.doubleToLongBits(category) != Double
				.doubleToLongBits(other.category))
			return false;
		if (Double.doubleToLongBits(group) != Double
				.doubleToLongBits(other.group))
			return false;
		if (Double.doubleToLongBits(rating) != Double
				.doubleToLongBits(other.rating))
			return false;
		return true;
	}
	
	

}
