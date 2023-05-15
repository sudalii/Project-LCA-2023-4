package org.earthster.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.earthster.client.io.ExchangeModel;
import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.model.Product;

/**
 * Some assertions for the export / import of RDF models.
 */
public class RdfAssert {

	/**
	 * Assert that the given model is empty but not <code>null</code>.
	 */
	public static void assertEmptyModel(ExchangeModel model) {
		assertNotNull(model);
		assertNull(model.getAssessment());
		assertNull(model.getProduct());
	}

	/**
	 * Assert that only the given product is transfered to the exchange model
	 * and that there is no assessment.
	 */
	public static void assertOnlyProduct(Product product, ExchangeModel model) {
		assertNull(model.getAssessment());
		assertEqualProducts(product, model.getProduct());
	}

	/**
	 * Assert that only the given assessment is transfered to the exchange model
	 * and that there is no product.
	 */
	public static void assertOnlyAssessment(Assessment assessment,
			ExchangeModel model) {
		assertNull(model.getProduct());
		assertEqualAssessments(assessment, model.getAssessment());
	}

	/**
	 * Assert that the given models are equal but not identical.
	 */
	public static void assertEqualModels(ExchangeModel expected,
			ExchangeModel actual) {
		assertEqualProducts(expected.getProduct(), actual.getProduct());
		assertEqualAssessments(expected.getAssessment(), actual.getAssessment());
	}

	/**
	 * Assert that the given products are equal but not identical.
	 */
	public static void assertEqualProducts(Product expected, Product actual) {
		Comparator comparator = new Comparator(expected, actual);
		assertTrue(comparator.areFieldsEqual("id", "name", "description",
				"commodityCode", "assessmentId"));
		assertTrue(expected != actual);
	}

	/**
	 * Assert that the given assessments are equal but not identical.
	 */
	public static void assertEqualAssessments(Assessment expected,
			Assessment actual) {
		assertTrue(expected != null && actual != null);
		assertTrue(expected != actual);
		assertTrue(areEqual(expected, actual));
	}

	/**
	 * Compares the assessments.
	 */
	private static boolean areEqual(Assessment assessment,
			Assessment assessmentCopy) {
		boolean equal = assessment.getId().equals(assessmentCopy.getId());
		Iterator<AssessmentResult> it = assessment.getResults().iterator();
		while (equal && it.hasNext()) {
			AssessmentResult result1 = it.next();
			AssessmentResult result2 = assessmentCopy.getResult(result1
					.getCategory().getId());
			Comparator comparator = new Comparator(result1.getCategory(),
					result2.getCategory());
			equal = comparator.areFieldsEqual()
					&& Math.abs(result1.getValue() - result2.getValue()) < 1e-6;
		}
		return equal;
	}

}
