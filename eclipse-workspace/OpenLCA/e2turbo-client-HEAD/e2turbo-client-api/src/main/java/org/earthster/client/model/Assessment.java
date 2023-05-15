package org.earthster.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * A set of impact assessment results of a product.
 */
@Entity
public class Assessment implements Copyable<Assessment>, IResource {

	private static final String RESOURCE_URI_PREFIX = "http://eps.earthster.org/eps/impactassessment/uuid/";

	@Id
	private String id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_ASSESSMENT")
	private List<AssessmentResult> results = new ArrayList<AssessmentResult>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<AssessmentResult> getResults() {
		return results;
	}

	/**
	 * Returns the result of the assessment for the given LCIA category ID.
	 */
	public AssessmentResult getResult(String categoryId) {
		for (AssessmentResult res : results) {
			if (res.getCategory() != null && res.getCategory().getId() != null
					&& res.getCategory().getId().equals(categoryId)) {
				return res;
			}
		}
		return null;
	}

	@Override
	public Assessment copy() {
		Assessment copy = new Assessment();
		copy.id = UUID.randomUUID().toString();
		for (AssessmentResult result : this.results) {
			copy.results.add(result.copy());
		}
		return copy;
	}

	@Override
	public String getPublicationUri() {
		// TODO: implement as field
		return null;
	}

	@Override
	public String getResourceUri() {
		return RESOURCE_URI_PREFIX + id;
	}

	/**
	 * Returns the resource URI for the given id.
	 */
	public static String getResourceUri(String id) {
		return RESOURCE_URI_PREFIX + id;
	}

	/**
	 * Extract the ID from the given resource URI.
	 */
	public static String getResourceId(String resourceUri) {
		String id = null;
		if (resourceUri != null && resourceUri.startsWith(RESOURCE_URI_PREFIX)) {
			id = resourceUri.substring(RESOURCE_URI_PREFIX.length());
		}
		return id;
	}

}
