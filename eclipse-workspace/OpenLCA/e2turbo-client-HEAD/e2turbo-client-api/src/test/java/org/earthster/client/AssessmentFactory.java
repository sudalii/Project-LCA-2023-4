package org.earthster.client;

import java.util.UUID;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.rdf.ImpactCategoryTerm;

/**
 * Creates sample assessment results.
 * 
 */
public class AssessmentFactory {

	private AssessmentFactory() {
	}

	/**
	 * Create a new assessment result.
	 */
	public static Assessment create() {
		Assessment assessment = new Assessment();
		assessment.setId(UUID.randomUUID().toString());
		createResults(assessment);
		return assessment;
	}

	/**
	 * Create the category results.
	 */
	private static void createResults(Assessment assessment) {
		for (ImpactCategoryTerm term : ImpactCategoryTerm.values()) {
			AssessmentResult result = new AssessmentResult();
			result.setId(UUID.randomUUID().toString());
			result.setValue(Math.random() * 10);
			result.setCategory(term.getCategory());
			assessment.getResults().add(result);
		}
	}
}
