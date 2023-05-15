package org.earthster.client.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractIndex<T> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Creates a document for the given item.
	 */
	protected abstract Document marshal(T item);

	protected abstract T unmarshal(Document doc);

	/**
	 * Creates the directory of the index.
	 */
	protected RAMDirectory makeDirectory(List<T> items) {
		RAMDirectory ramDir = new RAMDirectory();
		try {
			IndexWriter writer = new IndexWriter(ramDir, new StandardAnalyzer(
					Version.LUCENE_30), true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			for (T item : items) {
				Document doc = marshal(item);
				writer.addDocument(doc);
			}
			writer.optimize();
			writer.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ramDir;
	}

	protected abstract RAMDirectory getDirectory();

	protected abstract String getSearchField();

	public List<T> search(final String term, final int maxHits) {
		List<T> results = new ArrayList<T>();
		RAMDirectory dir = getDirectory();
		try {

			// create index search and query parser
			IndexSearcher searcher = new IndexSearcher(dir, true);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			QueryParser parser = new QueryParser(Version.LUCENE_30,
					getSearchField(), analyzer);

			// create the query
			String searchTerm = term;
			if (searchTerm != null && term.length() > 0) {
				char last = searchTerm.charAt(term.length() - 1);
				if (Character.isLetter(last)) {
					searchTerm = searchTerm + "*";
				}
			}
			Query query = parser.parse(searchTerm);

			// get the documents and create the models
			ScoreDoc[] docs = searcher.search(query, maxHits).scoreDocs;
			for (int i = 0; i < docs.length; i++) {
				Document doc = searcher.doc(docs[i].doc);
				T item = unmarshal(doc);
				if (item != null) {
					results.add(item);
				}
			}

			searcher.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return results;
	}

	public abstract void refresh();

}
