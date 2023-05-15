package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 */
public final class GoodRelations {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://purl.org/goodrelations/v1#";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "gr";

	/**  */
	public static final Resource ProductOrServiceModel = model
			.createResource(NS + "ProductOrServiceModel");

	/**  */
	public static final Resource BusinessEntity = model.createResource(NS
			+ "BusinessEntity");

	/**  */
	public static final Property hasManufacturer = model.createProperty(NS
			+ "hasManufacturer");

	/**  */
	public static final Property hasLegalName = model.createProperty(NS
			+ "hasLegalName");

}
