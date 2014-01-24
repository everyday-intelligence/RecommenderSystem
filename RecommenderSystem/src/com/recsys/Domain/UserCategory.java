package com.recsys.Domain;

public class UserCategory {

	private double userId;
	private double category;
	public UserCategory(double userId, double category) {
		super();
		this.userId = userId;
		this.category = category;
	}
	public double getGroup() {
		return userId;
	}
	public double getCategory() {
		return category;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(category);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(userId);
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
		UserCategory other = (UserCategory) obj;
		if (Double.doubleToLongBits(category) != Double
				.doubleToLongBits(other.category))
			return false;
		if (Double.doubleToLongBits(userId) != Double
				.doubleToLongBits(other.userId))
			return false;
		return true;
	}
	
	
	

}
