package org.earthster.client.database;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;
import org.earthster.client.model.Product;

/**
 * The Lucene search index for products.
 */
public class ProductIndex extends AbstractIndex<Product> {

	private static final String FIELD_NAME = "text";
	private static final String FIELD_ID = "id";

	private RAMDirectory directory;
	private Database database;

	public ProductIndex(Database database) {
		this.database = database;
		makeIndex();
	}

	private void makeIndex() {
		List<Product> products = database.selectAll(Product.class);
		directory = makeDirectory(products);
	}

	@Override
	protected Document marshal(Product product) {
		Document doc = new Document();
		doc.add(new Field(FIELD_NAME, product.getName(), Field.Store.YES,
				Field.Index.ANALYZED));
		doc.add(new Field(FIELD_ID, product.getId(), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		return doc;
	}

	@Override
	protected Product unmarshal(Document doc) {
		String productId = doc.get(FIELD_ID);
		Product product = database.select(Product.class, productId);
		return product;
	}

	@Override
	protected String getSearchField() {
		return FIELD_NAME;
	}

	@Override
	protected RAMDirectory getDirectory() {
		return directory;
	}

	@Override
	public void refresh() {
		makeIndex();
	}

}
