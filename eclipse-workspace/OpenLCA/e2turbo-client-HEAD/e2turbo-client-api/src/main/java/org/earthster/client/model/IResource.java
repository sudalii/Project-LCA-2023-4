package org.earthster.client.model;

/**
 * An interface for resources that can be published.
 */
public interface IResource {

	/**
	 * Get the URI of the resource.
	 */
	String getResourceUri();

	/**
	 * Get the URI of the publication where the resource is published.
	 */
	String getPublicationUri();

}
