package org.earthster.client.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Flows can be used in inputs and outputs of processes.
 */
@Entity
public class Flow implements Copyable<Flow> {

	@Transient
	public static final int PRODUCT = 1;

	@Transient
	public static final int ELEM_FLOW = 2;

	@Id
	private String id;

	private String name;

	private String unit;

	private String category;

	/**
	 * The ID of the assessment result of this flow. In case of an elementary
	 * flow this are the LCIA factors.
	 */
	@Column(name = "FK_ASSESSMENT")
	private String assessmentId;

	/**
	 * Indicates whether this flow is a product or elementary flow.
	 */
	private int type = 1;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Unit in which amounts of the flow are given.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Unit in which amounts of the flow are given.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * The identifier of the flow.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The identifier of the flow.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isProduct() {
		return this.type == PRODUCT;
	}

	public boolean isElemFlow() {
		return this.type == ELEM_FLOW;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public Flow copy() {
		Flow copy = new Flow();
		copy.assessmentId = this.assessmentId;
		copy.category = this.category;
		copy.id = UUID.randomUUID().toString();
		copy.name = this.name;
		copy.type = this.type;
		copy.unit = this.unit;
		return copy;
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
		Flow other = (Flow) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
