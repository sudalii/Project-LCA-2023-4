package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 */
public final class Units {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://ontology.earthster.org/eco/unit#";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "unit";

	/**  */
	public static final Resource kgCO2Equiv = model.createResource(NS
			+ "kgCO2Equiv");

	/**  */
	public static final Resource daly = model.createResource(NS + "daly");

	/**  */
	public static final Resource pdfM2Year = model.createResource(NS
			+ "pdfM2Year");

	/**  */
	public static final Resource MJPrimaryNRE = model.createResource(NS
			+ "MJPrimaryNRE");

}
