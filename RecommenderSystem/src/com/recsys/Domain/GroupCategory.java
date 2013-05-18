package com.recsys.Domain;

public class GroupCategory {

	private double group;
	private double category;
	public GroupCategory(double group, double category) {
		super();
		this.group = group;
		this.category = category;
	}
	public double getGroup() {
		return group;
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
		temp = Double.doubleToLongBits(group);
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
		GroupCategory other = (GroupCategory) obj;
		if (Double.doubleToLongBits(category) != Double
				.doubleToLongBits(other.category))
			return false;
		if (Double.doubleToLongBits(group) != Double
				.doubleToLongBits(other.group))
			return false;
		return true;
	}
	

	
	

}
