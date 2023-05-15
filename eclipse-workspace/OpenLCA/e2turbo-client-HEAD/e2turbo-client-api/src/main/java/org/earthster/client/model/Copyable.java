package org.earthster.client.model;

public interface Copyable<T> {

	/**
	 * Get a deep copy.
	 */
	T copy();

}
