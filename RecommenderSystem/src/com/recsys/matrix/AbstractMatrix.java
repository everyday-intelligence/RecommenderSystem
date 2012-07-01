/**
 * 
 */
package com.recsys.matrix;

/**
 * @author Ramzi
 *
 */
public abstract class AbstractMatrix {
	
	protected abstract Double get(int i, int j);
	protected abstract boolean set(int i, int j, Double vals);

}
