package org.earthster.client.io;

import org.earthster.client.rdf.RdfAssessmentWriter;
import org.earthster.client.rdf.RdfModel;
import org.earthster.client.rdf.RdfProductWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * The template for an RDF export.
 */
public abstract class RdfExport {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private ExchangeModel exchangeModel;

	/**
	 * Creates a new instance of the export.
	 */
	public RdfExport(ExchangeModel model) {
		this.exchangeModel = model;
	}

	/**
	 * Run the export.
	 */
	public String run() {
		log.debug("Run export");
		Model model = createModel();
		return writeModel(model);
	}

	/**
	 * Write the model to the export location and returns an URI of the written
	 * resource.
	 */
	protected abstract String writeModel(Model model);

	/**
	 * Create the RDF model with the given product and assessment.
	 */
	private Model createModel() {
		Model model = RdfModel.createModel();
		RdfProductWriter productWriter = new RdfProductWriter(model);
		productWriter.write(exchangeModel.getProduct());
		RdfAssessmentWriter assessmentWriter = new RdfAssessmentWriter(model);
		assessmentWriter.write(exchangeModel.getAssessment());
		return model;
	}
}
