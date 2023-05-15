package org.earthster.client;

import static org.earthster.client.RdfAssert.assertEqualProducts;

import org.earthster.client.model.Product;
import org.earthster.client.rdf.RdfModel;
import org.earthster.client.rdf.RdfProductReader;
import org.earthster.client.rdf.RdfProductWriter;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Test the de-/serialization of products from/to RDF.
 */
public class RdfProductTest {

	private RdfProductWriter writer;
	private RdfProductReader reader;

	@Before
	public void setUp() {
		Model model = RdfModel.createModel();
		writer = new RdfProductWriter(model);
		reader = new RdfProductReader(model);
	}

	@Test
	public void testWriteReadProduct() {
		Product product = Generator.create(Product.class);
		writer.write(product);
		Product productCopy = reader.getProduct();
		assertEqualProducts(product, productCopy);
	}

}
