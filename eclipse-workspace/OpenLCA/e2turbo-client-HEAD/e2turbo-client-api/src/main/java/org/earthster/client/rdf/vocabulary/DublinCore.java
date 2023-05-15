package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * 
 */
public final class DublinCore {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://purl.org/dc/elements/1.1/";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "dc";

	/**  */
	public static final Property description = model.createProperty(NS
			+ "description");

	/**  */
	public static final Property date = model.createProperty(NS + "date");

}
