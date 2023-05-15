package org.earthster.client.rdf;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.Product;
import org.earthster.client.rdf.vocabulary.DublinCore;
import org.earthster.client.rdf.vocabulary.ECO;
import org.earthster.client.rdf.vocabulary.GoodRelations;
import org.earthster.client.rdf.vocabulary.RDFS;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A class for the conversion of products into RDF models.
 */
public class RdfProductWriter {

	private Model model;

	/**
	 * Create a new writer.
	 */
	public RdfProductWriter(Model model) {
		this.model = model;
	}

	/**
	 * Write the product to the RDF model.
	 */
	public void write(Product product) {

		if (product == null || product.getId() == null)
			return;

		String uri = product.getResourceUri();
		Resource resource = model.createResource(uri);
		addProductAttributes(product, resource);
		addAssessmentReference(product, resource);

	}

	/**
	 * Add a reference to an assessment resource if the product has an
	 * assessment result.
	 */
	private void addAssessmentReference(Product product, Resource resource) {
		if (product.getAssessmentId() != null) {
			String assessmentUri = Assessment.getResourceUri(product
					.getAssessmentId());
			resource.addProperty(ECO.hasImpactAssessment,
					model.createResource(assessmentUri));
		}
	}

	/**
	 * Add general attributes to the product resource.
	 */
	private void addProductAttributes(Product product, Resource resource) {

		// type
		resource.addProperty(RDF.type, GoodRelations.ProductOrServiceModel);

		writeString(product.getName(), RDFS.label, resource);
		writeString(product.getId(), ECO.hasUUID, resource);
		writeString(product.getCommodityCode(), ECO.hasCategory, resource);
		writeString(product.getDescription(), DublinCore.description, resource);
	}

	/**
	 * Writes the string as the given property to the resource.
	 */
	private void writeString(String string, Property property, Resource resource) {
		if (string != null) {
			resource.addProperty(property, string);
		}
	}

}
