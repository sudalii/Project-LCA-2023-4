package org.earthster.client;

import static org.earthster.client.RdfAssert.assertEqualAssessments;

import org.earthster.client.model.Assessment;
import org.earthster.client.rdf.RdfAssessmentReader;
import org.earthster.client.rdf.RdfAssessmentWriter;
import org.earthster.client.rdf.RdfModel;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Test the de-/serialization of assessments from/to RDF.
 */
public class RdfAssessmentTest {

	private RdfAssessmentReader reader;
	private RdfAssessmentWriter writer;

	@Before
	public void setUp() {
		Model model = RdfModel.createModel();
		reader = new RdfAssessmentReader(model);
		writer = new RdfAssessmentWriter(model);
	}

	@Test
	public void testReadWriteAssessment() {
		Assessment assessment = AssessmentFactory.create();
		writer.write(assessment);
		Assessment assessmentCopy = reader.getAssessment();
		assertEqualAssessments(assessment, assessmentCopy);
	}

}
