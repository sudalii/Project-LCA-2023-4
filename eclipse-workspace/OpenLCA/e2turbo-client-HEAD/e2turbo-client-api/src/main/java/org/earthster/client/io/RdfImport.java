package org.earthster.client.io;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.Product;
import org.earthster.client.rdf.RdfAssessmentReader;
import org.earthster.client.rdf.RdfProductReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * The template for an RDF import.
 */
public abstract class RdfImport {

	/**
	 * Read the model from the respective provider.
	 */
	protected abstract Model readModel();

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Runs the import.
	 */
	public ExchangeModel getModel() {
		log.debug("Run import");
		Model model = readModel();
		ExchangeModel exchangeModel = createExchangeModel(model);
		return exchangeModel;
	}

	/**
	 * Create the exchange model from the RDF model.
	 */
	private ExchangeModel createExchangeModel(Model model) {
		RdfProductReader productReader = new RdfProductReader(model);
		Product product = productReader.getProduct();
		RdfAssessmentReader assessmentReader = new RdfAssessmentReader(model);
		Assessment assessment = assessmentReader.getAssessment();
		ExchangeModel exchangeModel = new ExchangeModel(product, assessment);
		return exchangeModel;
	}

}
