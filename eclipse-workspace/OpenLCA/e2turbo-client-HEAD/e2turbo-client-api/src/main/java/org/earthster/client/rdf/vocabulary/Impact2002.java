package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 */
public final class Impact2002 {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://ontology.earthster.org/eco/impact2002Plus#";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "im2002";

	/**  */
	public static final Resource ImpactAssessment = model.createResource(NS
			+ "ImpactAssessment");

	/**  */
	public static final Resource impact2002Plus = model.createResource(NS
			+ "impact2002Plus");

	/**  */
	public static final Resource cdClimateChange = model.createResource(NS
			+ "cdClimateChange");

	/**  */
	public static final Resource cdEcosystemQuality = model.createResource(NS
			+ "cdEcosystemQuality");

	/**  */
	public static final Resource cdHumanHealth = model.createResource(NS
			+ "cdHumanHealth");

	/**  */
	public static final Resource cdResources = model.createResource(NS
			+ "cdResources");

}
