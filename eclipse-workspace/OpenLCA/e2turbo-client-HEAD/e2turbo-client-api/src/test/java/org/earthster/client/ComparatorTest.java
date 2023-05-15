package org.earthster.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.model.Exchange;
import org.junit.Test;

/**
 * Tests for the comparator.
 */
public class ComparatorTest {

	private Comparator comparator;

	@Test
	public void testEqual() {
		Exchange exchange1 = Generator.create(Exchange.class);
		Exchange exchange2 = exchange1.copy();
		exchange2.setId(exchange1.getId());
		compareExchanges(exchange1, exchange2);
	}

	@Test
	public void testNullFields() {
		Exchange exchange1 = new Exchange();
		Exchange exchange2 = new Exchange();
		compareExchanges(exchange1, exchange2);
	}

	@Test
	public void testNotEqual() {
		Exchange exchange1 = Generator.create(Exchange.class);
		Exchange exchange2 = exchange1.copy();
		exchange2.setAmount(exchange1.getAmount() + 42);
		comparator = new Comparator(exchange1, exchange2);
		assertFalse(comparator.areFieldsEqual());
	}

	@Test
	public void testEqualViaNames() {
		Exchange exchange1 = Generator.create(Exchange.class);
		Exchange exchange2 = exchange1.copy();
		comparator = new Comparator(exchange1, exchange2);
		assertTrue(comparator.areFieldsEqual("input", "amount", "flow"));
	}

	@Test
	public void testEqualLists() {
		Assessment assessment = Generator.create(Assessment.class);
		Assessment assessmentCopy = new Assessment();
		assessmentCopy.setId(assessment.getId());
		assessmentCopy.getResults().addAll(assessment.getResults());

		comparator = new Comparator(assessment, assessmentCopy);
		assertTrue(assessment.getResults().size() > 0);
		assertTrue(comparator.areFieldsEqual());
	}

	@Test
	public void testNotEqualLists() {
		Assessment assessment = Generator.create(Assessment.class);
		Assessment assessmentCopy = new Assessment();
		assessmentCopy.setId(assessment.getId());
		assessmentCopy.getResults().addAll(assessment.getResults());
		assessmentCopy.getResults().remove(0);
		assessmentCopy.getResults().add(
				Generator.create(AssessmentResult.class));

		Comparator comparator = new Comparator(assessment, assessmentCopy);
		assertTrue(assessment.getResults().size() > 0);
		assertFalse(comparator.areFieldsEqual());
	}

	@Test
	public void testEmptyList() {
		Assessment assessment = Generator.create(Assessment.class);
		Assessment assessmentCopy = new Assessment();
		assessmentCopy.setId(assessment.getId());

		Comparator comparator = new Comparator(assessment, assessmentCopy);
		assertFalse(comparator.areFieldsEqual());
	}

	private void compareExchanges(Exchange exchange1, Exchange exchange2) {
		comparator = new Comparator(exchange1, exchange2);
		assertTrue(comparator.areFieldsEqual());
	}

}
