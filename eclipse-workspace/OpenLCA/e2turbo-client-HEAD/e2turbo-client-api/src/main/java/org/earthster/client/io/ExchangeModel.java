package org.earthster.client.io;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.Product;

/**
 * The model for the data exchange.
 */
public class ExchangeModel {

	private Product product;

	private Assessment assessment;

	public ExchangeModel() {
	}

	public ExchangeModel(Product product, Assessment assessment) {
		this.assessment = assessment;
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

}
