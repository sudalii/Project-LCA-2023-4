package org.earthster.client.rdf;

import java.util.ArrayList;
import java.util.List;

import org.earthster.client.model.Assessment;
import org.earthster.client.model.Product;
import org.earthster.client.model.RetrievalType;
import org.earthster.client.rdf.vocabulary.DublinCore;
import org.earthster.client.rdf.vocabulary.ECO;
import org.earthster.client.rdf.vocabulary.GoodRelations;
import org.earthster.client.rdf.vocabulary.RDFS;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A reader for products from RDF models.
 */
public class RdfProductReader {

	private Model model;

	/**
	 * Creates a new reader.
	 */
	public RdfProductReader(Model model) {
		this.model = model;
	}

	/**
	 * Read a product from the model.
	 */
	public Product getProduct() {
		Product product = null;
		List<Product> products = getProducts();
		if (products.size() > 0) {
			product = products.get(0);
		}
		return product;
	}

	/**
	 * Read the products from the model.
	 */
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		ResIterator it = model.listSubjectsWithProperty(RDF.type,
				GoodRelations.ProductOrServiceModel);
		while (it.hasNext()) {
			Resource productResource = it.next();
			Product product = makeProduct(productResource);
			if (product != null)
				products.add(product);
		}
		return products;
	}

	/**
	 * Creates the product model from the given resource. If this is no valid
	 * product resource, <code>null</code> is returned.
	 */
	private Product makeProduct(Resource resource) {
		Product product = new Product();
		product.setRetrievalType(RetrievalType.IMPORTED);
		setProductAttributes(resource, product);
		setAssessmentId(resource, product);
		return product;
	}

	/**
	 * Set the product attributes.
	 */
	private void setProductAttributes(Resource resource, Product product) {
		String name = getStringValue(resource, RDFS.label);
		product.setName(name);

		String uuid = getStringValue(resource, ECO.hasUUID);
		product.setId(uuid);

		String description = getStringValue(resource, DublinCore.description);
		product.setDescription(description);

		String code = getStringValue(resource, ECO.hasCategory);
		product.setCommodityCode(code);
	}

	/**
	 * Get the string value for the property of the given resource.
	 */
	private String getStringValue(Resource resource, Property property) {
		String val = null;
		Statement statement = resource.getProperty(property);
		if (statement != null && statement.getObject().isLiteral()) {
			val = statement.getObject().asLiteral().getString();
		}
		return val;
	}

	/**
	 * Set the assessment ID if there is a reference to an assessment result in
	 * the resource.
	 */
	private void setAssessmentId(Resource resource, Product product) {
		Statement statement = resource.getProperty(ECO.hasImpactAssessment);
		if (statement != null && statement.getObject().isResource()) {
			String assessmentUri = statement.getObject().asResource().getURI();
			String assessmentId = Assessment.getResourceId(assessmentUri);
			product.setAssessmentId(assessmentId);
		}
	}

}
