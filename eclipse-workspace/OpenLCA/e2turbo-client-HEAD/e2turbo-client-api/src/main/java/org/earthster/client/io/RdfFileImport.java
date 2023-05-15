package org.earthster.client.io;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Imports products and assessments from an RDF file into the database.
 */
public class RdfFileImport extends RdfImport {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private File file;

	/**
	 * Creates a new instance of the RDF import.
	 * 
	 * @param file
	 *            the file with the products and assessments to be imported.
	 */
	public RdfFileImport(File file) {
		this.file = file;
	}

	/**
	 * Read the RDF model from the file.
	 */
	@Override
	protected Model readModel() {
		Model model = ModelFactory.createDefaultModel();
		try {
			model.read(new FileInputStream(file), null, "TURTLE");
		} catch (Exception e) {
			log.error("Cannot read file: " + e.getMessage(), e);
		}
		return model;
	}

}
