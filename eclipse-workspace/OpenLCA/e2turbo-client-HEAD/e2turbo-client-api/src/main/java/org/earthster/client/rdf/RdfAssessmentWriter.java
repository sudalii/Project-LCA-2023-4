package org.earthster.client.rdf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.model.ImpactCategory;
import org.earthster.client.rdf.vocabulary.DBPedia;
import org.earthster.client.rdf.vocabulary.DublinCore;
import org.earthster.client.rdf.vocabulary.ECO;
import org.earthster.client.rdf.vocabulary.Impact2002;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A writer class for the serialisation of assessments to RDF.
 */
public class RdfAssessmentWriter {

	private Model model;

	/**
	 * Creates a new writer instance.
	 */
	public RdfAssessmentWriter(Model model) {
		this.model = model;
	}

	/**
	 * Write the assessment to the model.
	 */
	public void write(Assessment assessment) {
		if (assessment == null || assessment.getId() == null)
			return;

		Resource resource = model.createResource(assessment.getResourceUri());
		addAssessmentAttributes(assessment, resource);
		addQuantitativeReference(resource);
		addAssessmentResults(assessment, resource);
	}

	/**
	 * Add general assessment attributes to the assessment resource.
	 */
	private void addAssessmentAttributes(Assessment assessment,
			Resource resource) {
		// type
		resource.addProperty(RDF.type, ECO.ImpactAssessment);
		resource.addProperty(RDF.type, Impact2002.ImpactAssessment);

		// TODO: time stamp in model
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		resource.addProperty(DublinCore.date, model.createTypedLiteral(date,
				"http://www.w3.org/2001/XMLSchema#date"));
		// UUID
		resource.addProperty(ECO.hasUUID, assessment.getId());

		// method
		resource.addProperty(ECO.hasImpactAssessmentMethod,
				Impact2002.impact2002Plus);
	}

	/**
	 * Add the quantitative reference to the assessment resource.
	 */
	private void addQuantitativeReference(Resource assessmentResource) {
		Resource qRes = model.createResource();
		qRes.addProperty(ECO.hasMagnitude, model.createTypedLiteral(1));
		qRes.addProperty(ECO.hasUnitOfMeasure, DBPedia.USD);
		assessmentResource.addProperty(ECO.quantityAssessed, qRes);
	}

	/**
	 * Add the assessment results to the assessment resource.
	 */
	private void addAssessmentResults(Assessment assessment, Resource resource) {
		for (AssessmentResult result : assessment.getResults()) {
			ImpactCategory category = result.getCategory();
			if (isValid(category)) {
				Resource resultResource = createCategoryResultResource(result);
				Resource quanRes = createResultQuantityResource(result);
				resultResource.addProperty(ECO.hasQuantity, quanRes);
				resource.addProperty(ECO.hasImpactCategoryIndicatorResult,
						resultResource);
			}
		}
	}

	/**
	 * Returns true if the given LCIA category is valid.
	 */
	private boolean isValid(ImpactCategory category) {
		return category != null && category.getName() != null
				&& category.getUnit() != null;
	}

	/**
	 * Create a resource for a category result.
	 */
	private Resource createCategoryResultResource(AssessmentResult result) {
		Resource resource = model.createResource();
		ImpactCategoryTerm term = ImpactCategoryTerm.fromCategory(result
				.getCategory());
		if (term != null) {
			resource.addProperty(
					ECO.hasImpactAssessmentMethodCategoryDescription,
					term.getCategoryResource());
		}
		return resource;
	}

	/**
	 * Create a resource for the quantity of a category result.
	 */
	private Resource createResultQuantityResource(AssessmentResult result) {
		Resource resource = model.createResource();
		resource.addLiteral(ECO.hasMagnitude, (float) result.getValue());
		ImpactCategoryTerm term = ImpactCategoryTerm.fromCategory(result
				.getCategory());
		if (term != null) {
			resource.addProperty(ECO.hasUnitOfMeasure, term.getUnitResource());
		}
		return resource;
	}
}
