package org.earthster.client.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A single impact assessment category result of a product assessment.
 */
@Entity
@Table(name = "ASSESSMENT_RESULT")
public class AssessmentResult implements Copyable<AssessmentResult> {

	@Id
	private String id;

	@OneToOne
	@JoinColumn(name = "FK_LCIA_CATEGORY")
	private ImpactCategory category;

	private double value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ImpactCategory getCategory() {
		return category;
	}

	public void setCategory(ImpactCategory category) {
		this.category = category;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public AssessmentResult copy() {
		AssessmentResult copy = new AssessmentResult();
		copy.category = this.category;
		copy.id = UUID.randomUUID().toString();
		copy.value = this.value;
		return copy;
	}
}
