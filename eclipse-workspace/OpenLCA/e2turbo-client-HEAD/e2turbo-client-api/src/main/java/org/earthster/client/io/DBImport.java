package org.earthster.client.io;

import org.earthster.client.database.Database;
import org.earthster.client.model.Assessment;
import org.earthster.client.model.BeaCommodity;
import org.earthster.client.model.Flow;
import org.earthster.client.model.Product;
import org.earthster.client.model.RetrievalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The database import.
 */
public class DBImport {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Database database;
	private ExchangeModel model;

	/**
	 * Creates a new instance of the data import.
	 */
	public DBImport(Database database, ExchangeModel model) {
		this.database = database;
		this.model = model;
	}

	/**
	 * Run the import.
	 */
	public void run() {
		if (!isModelValid()) {
			log.error("Import failed: Format not valid.");
		} else if (dbContainsModel()) {
			log.error("Import failed: Database already "
					+ "contains product or assessment "
					+ model.getProduct().getName() + ".");
		} else {
			tryImport();
		}
	}

	/**
	 * Tests if the model is valid for the import.
	 */
	private boolean isModelValid() {
		return model.getAssessment() != null
				&& model.getAssessment().getId() != null
				&& model.getProduct() != null
				&& model.getProduct().getId() != null;
	}

	/**
	 * Tests if the database already contains the model.
	 */
	private boolean dbContainsModel() {
		return database.contains(Product.class, model.getProduct().getId())
				|| database.contains(Assessment.class, model.getAssessment()
						.getId())
				|| database.contains(Flow.class, model.getProduct().getId());
	}

	/**
	 * Try to import the exchange model.
	 */
	private void tryImport() {
		try {
			model.getProduct().setRetrievalType(RetrievalType.IMPORTED);
			Flow flow = makeFlow(model.getProduct());
			database.insert(model.getProduct());
			database.insert(flow);
			database.insert(model.getAssessment());
		} catch (Exception e) {
			log.error("Import failed: " + e.getMessage(), e);
		}
	}

	/**
	 * Creates the respective flow object for the given product.
	 */
	private Flow makeFlow(Product product) {
		Flow flow = new Flow();
		flow.setAssessmentId(product.getAssessmentId());
		if (product.getCommodityCode() != null) {
			BeaCommodity commodity = database.select(BeaCommodity.class,
					product.getCommodityCode());
			if (commodity != null) {
				flow.setCategory(commodity.getName());
			}
		}
		flow.setId(product.getId());
		flow.setName(product.getName());
		flow.setType(Flow.PRODUCT);
		flow.setUnit("USD");
		return flow;
	}

}
