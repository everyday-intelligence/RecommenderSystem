package com.recsys.utils;

public interface Checker<T> {
	public boolean verifyPredicate(T obj);
}
