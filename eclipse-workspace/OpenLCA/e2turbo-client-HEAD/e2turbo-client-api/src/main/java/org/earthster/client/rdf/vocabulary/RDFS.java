package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * 
 */
public final class RDFS {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://www.w3.org/2000/01/rdf-schema#";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "rdfs";

	/**  */
	public static final Property label = model.createProperty(NS + "label");

}
