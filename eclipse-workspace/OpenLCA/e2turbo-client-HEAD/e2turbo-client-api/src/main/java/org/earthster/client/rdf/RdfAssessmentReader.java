package org.earthster.client.rdf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.model.ImpactCategory;
import org.earthster.client.rdf.vocabulary.ECO;
import org.earthster.client.rdf.vocabulary.Impact2002;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A reader for assessments from RDF models.
 */
public class RdfAssessmentReader {

	private Model model;

	/**
	 * Creates a new reader.
	 */
	public RdfAssessmentReader(Model model) {
		this.model = model;
	}

	/**
	 * Read an assessment from the model.
	 */
	public Assessment getAssessment() {
		Assessment assessment = null;
		List<Assessment> assessments = getAssessments();
		if (!assessments.isEmpty()) {
			assessment = assessments.get(0);
		}
		return assessment;
	}

	/**
	 * Read the assessments from the model.
	 */
	public List<Assessment> getAssessments() {
		List<Assessment> assessments = new ArrayList<Assessment>();
		ResIterator it = model.listSubjectsWithProperty(RDF.type,
				Impact2002.ImpactAssessment);
		while (it.hasNext()) {
			Resource resource = it.next();
			Assessment assessment = makeAssessment(resource);
			if (assessment != null) {
				assessments.add(assessment);
			}
		}
		return assessments;
	}

	/**
	 * Creates the assessment model from the given resource.
	 */
	private Assessment makeAssessment(Resource resource) {
		Assessment assessment = new Assessment();
		addUuid(resource, assessment);
		addResults(resource, assessment);
		return assessment;
	}

	/**
	 * Get the UUID from the resource and add it to the assessment.
	 */
	private void addUuid(Resource resource, Assessment assessment) {
		Statement st = resource.getProperty(ECO.hasUUID);
		if (st != null && st.getObject().isLiteral()) {
			String uuid = st.getObject().asLiteral().getString();
			assessment.setId(uuid);
		}
	}

	/**
	 * Add the category results from the resource to the assessment.
	 */
	private void addResults(Resource resource, Assessment assessment) {
		for (ImpactCategoryTerm categoryTerm : ImpactCategoryTerm.values()) {
			ImpactCategory category = categoryTerm.getCategory();
			AssessmentResult result = new AssessmentResult();
			result.setCategory(category);
			result.setId(UUID.randomUUID().toString());
			result.setValue(selectResult(categoryTerm, resource));
			assessment.getResults().add(result);
		}
	}

	/**
	 * Selects the category result resource for the given LCIA category.
	 */
	private double selectResult(ImpactCategoryTerm categoryTerm,
			Resource assessmentResource) {

		double value = 0;

		StmtIterator it = assessmentResource
				.listProperties(ECO.hasImpactCategoryIndicatorResult);
		while (it.hasNext()) {

			Statement resultSt = it.next();
			if (resultSt.getObject().isResource()) {

				Resource resultResource = resultSt.getObject().asResource();
				boolean select = false;

				// compare the category information
				Statement categorySt = resultResource
						.getProperty(ECO.hasImpactAssessmentMethodCategoryDescription);
				if (categorySt != null && categorySt.getObject().isResource()) {
					Resource categoryResource = categorySt.getObject()
							.asResource();
					select = categoryTerm.getCategoryResource().equals(
							categoryResource);
				}

				// select the value
				if (select) {
					value = getMagnitude(resultResource);
				}
			}
		}

		return value;

	}

	/**
	 * Get the "magnitude" value from the result resource.
	 */
	private double getMagnitude(Resource resultResource) {
		double value = 0d;
		Statement quanSt = resultResource.getProperty(ECO.hasQuantity);
		if (quanSt != null && quanSt.getObject().isResource()) {
			Statement magnSt = quanSt.getObject().asResource()
					.getProperty(ECO.hasMagnitude);
			if (magnSt != null && magnSt.getObject().isLiteral()) {
				value = magnSt.getObject().asLiteral().getFloat();
			}
		}
		return value;
	}

}
