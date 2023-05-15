package org.earthster.client.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

@Entity
public class Product implements Copyable<Product>, IResource {

	private static final String RESOURCE_URI_PREFIX = "http://eps.earthster.org/eps/product/uuid/";

	@Id
	private String id;

	private String name;

	@Column(name = "FK_ASSESSMENT")
	private String assessmentId;

	@Column(name = "FK_LOCAL_ASSESSMENT")
	private String localAssessmentId;

	@Column(name = "FK_ACTIVITY")
	private String activityId;

	@Lob
	private String description;

	private String commodityCode;

	private int retrievalType = 1;

	@Transient
	private String publicationUri;
	
	public String getLocalAssessmentId() {
		return localAssessmentId;
	}

	public void setLocalAssessmentId(String localAssessmentId) {
		this.localAssessmentId = localAssessmentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public int getRetrievalType() {
		return retrievalType;
	}

	public void setRetrievalType(int retrievalType) {
		this.retrievalType = retrievalType;
	}

	@Override
	public Product copy() {
		Product copy = new Product();
		copy.activityId = this.activityId;
		copy.assessmentId = this.assessmentId;
		copy.commodityCode = this.commodityCode;
		copy.description = this.description;
		copy.id = UUID.randomUUID().toString();
		copy.localAssessmentId = this.localAssessmentId;
		copy.name = this.name;
		copy.retrievalType = this.retrievalType;
		return copy;
	}

	@Override
	public String getPublicationUri() {
		return publicationUri;
	}
	
	public void setPublicationUri(String publicationUri) {
		this.publicationUri = publicationUri;
	}

	@Override
	public String getResourceUri() {
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
	
	public static String getResourceUri(String productId) {
		return RESOURCE_URI_PREFIX + productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
