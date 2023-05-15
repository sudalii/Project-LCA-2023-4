package org.earthster.client;

import static org.earthster.client.RdfAssert.assertEmptyModel;
import static org.earthster.client.RdfAssert.assertEqualModels;
import static org.earthster.client.RdfAssert.assertOnlyAssessment;
import static org.earthster.client.RdfAssert.assertOnlyProduct;

import java.io.File;

import org.earthster.client.io.ExchangeModel;
import org.earthster.client.io.RdfFileExport;
import org.earthster.client.io.RdfFileImport;
import org.earthster.client.model.Assessment;
import org.earthster.client.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests import / export of RDF models from / to files.
 */
public class FileIOTest {

	private File file;

	@Before
	public void setUp() throws Exception {
		file = File.createTempFile("e-client-iotest", ".rdf");
	}

	@After
	public void tearDown() {
		// file.delete();
	}

	@Test
	public void testReadEmptyFile() throws Exception {
		ExchangeModel model = runFileImport();
		assertEmptyModel(model);
	}

	@Test
	public void testWriteReadEmptyModel() throws Exception {
		runFileExport(new ExchangeModel());
		ExchangeModel model = runFileImport();
		assertEmptyModel(model);
	}

	@Test
	public void testWriteReadOnlyProduct() {
		Product product = Generator.create(Product.class);
		runFileExport(new ExchangeModel(product, null));
		ExchangeModel model = runFileImport();
		assertOnlyProduct(product, model);
	}

	@Test
	public void testWriteReadOnlyAssessment() {
		Assessment assessment = AssessmentFactory.create();
		runFileExport(new ExchangeModel(null, assessment));
		ExchangeModel model = runFileImport();
		assertOnlyAssessment(assessment, model);
	}

	@Test
	public void testWriteReadModel() {
		Product product = Generator.create(Product.class);
		Assessment assessment = AssessmentFactory.create();
		ExchangeModel outModel = new ExchangeModel(product, assessment);
		runFileExport(outModel);
		ExchangeModel inModel = runFileImport();
		assertEqualModels(outModel, inModel);
	}

	private ExchangeModel runFileImport() {
		RdfFileImport fileImport = new RdfFileImport(file);
		ExchangeModel model = fileImport.getModel();
		return model;
	}

	private void runFileExport(ExchangeModel model) {
		RdfFileExport export = new RdfFileExport(model, file);
		export.run();
	}

}
