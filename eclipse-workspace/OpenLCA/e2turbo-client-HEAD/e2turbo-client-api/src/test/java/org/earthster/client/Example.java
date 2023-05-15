package org.earthster.client;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.AssessmentResult;
import org.earthster.client.rdf.ImpactCategoryTerm;
import org.earthster.client.rdf.RdfAssessmentWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Example {

	public static void main(String[] args) {

		// create a simple LCIA result
		Assessment assessment = new Assessment();
		assessment.setId("id-of-LCIA-result");
		AssessmentResult r1 = new AssessmentResult();
		r1.setCategory(ImpactCategoryTerm.ClimateChange.getCategory());
		r1.setId("r1");
		r1.setValue(42);
		assessment.getResults().add(r1);

		// write it to a model
		Model model = ModelFactory.createDefaultModel();
		RdfAssessmentWriter writer = new RdfAssessmentWriter(model);
		writer.write(assessment);

		// print it on the console
		model.write(System.out, "TURTLE");

	}

}
