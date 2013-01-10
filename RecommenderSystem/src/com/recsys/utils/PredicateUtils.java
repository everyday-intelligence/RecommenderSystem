package com.recsys.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredicateUtils {

	// put this in some class
	public static <T> Collection<T> findAll(Collection<T> coll, Checker<T> chk) {
	    List<T> l = new ArrayList<T>();
	    for (T obj : coll) {
	         if (chk.verifyPredicate(obj))
	             l.add(obj);
	    }
	    return l;
	}
	
//	public static <T> boolean verifyPredicate(T obj, Checker<T> chk){
//		return chk.verifyPredicate(obj);
//	}
//	
}
