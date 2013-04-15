/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.common.random;

/**
 * Interface for accessing a list of arbitrary elements randomly.
 * 
 */
public interface Randomizable<E> {

	/**
	 * Return the next randomly chosen element
	 */
	E get();

	/**
	 * Returns true if all included elements were returned at least once.
	 */
	boolean isAllHit();

	/**
	 * Reset to original state.
	 */
	void reset();

	/**
	 * Returns the number of handled elements.
	 */
	int size();
}