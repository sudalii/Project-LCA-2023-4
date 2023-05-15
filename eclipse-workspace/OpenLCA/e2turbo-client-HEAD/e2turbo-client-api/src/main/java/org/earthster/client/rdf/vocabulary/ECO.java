package org.earthster.client.rdf.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 */
public final class ECO {

	/** The RDF model that holds the vocabulary terms. */
	private static final Model model = ModelFactory.createDefaultModel();

	/** The name-space of the vocabulary as a string. */
	public static final String NS = "http://ontology.earthster.org/eco/core#";

	/** The prefix of the vocabulary as string. */
	public static final String PREFIX = "eco";

	/**  */
	public static final Resource ImpactAssessment = model.createResource(NS
			+ "ImpactAssessment");

	/**  */
	public static final Property hasCategory = model.createProperty(NS
			+ "hasCategory");

	/**  */
	public static final Property hasUUID = model.createProperty(NS + "hasUUID");

	/**  */
	public static final Property hasImpactAssessment = model.createProperty(NS
			+ "hasImpactAssessment");

	/**  */
	public static final Property hasImpactAssessmentMethod = model
			.createProperty(NS + "hasImpactAssessmentMethod");

	/**  */
	public static final Property quantityAssessed = model.createProperty(NS
			+ "quantityAssessed");

	/**  */
	public static final Property hasMagnitude = model.createProperty(NS
			+ "hasMagnitude");

	/**  */
	public static final Property hasUnitOfMeasure = model.createProperty(NS
			+ "hasUnitOfMeasure");

	/**  */
	public static final Property hasImpactCategoryIndicatorResult = model
			.createProperty(NS + "hasImpactCategoryIndicatorResult");

	/**  */
	public static final Property hasImpactAssessmentMethodCategoryDescription = model
			.createProperty(NS + "hasImpactAssessmentMethodCategoryDescription");

	/**  */
	public static final Property hasQuantity = model.createProperty(NS
			+ "hasQuantity");

}
