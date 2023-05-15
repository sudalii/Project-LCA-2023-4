package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 */
public final class DBPedia {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://dbpedia.org/resource/";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "dbpedia";

	/**  */
	public static final Resource USD = model.createResource(NS + "U.S._dollar");

}
