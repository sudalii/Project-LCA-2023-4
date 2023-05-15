package org.earthster.client.io;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * A class for the RDF export of entities to a file.
 */
public class RdfFileExport extends RdfExport {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private File file;

	/**
	 * Creates a new instance of the file export.
	 */
	public RdfFileExport(ExchangeModel model, File file) {
		super(model);
		this.file = file;
	}

	/**
	 * Write the RDF model to a file.
	 */
	@Override
	protected String writeModel(Model model) {
		String uri = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			model.write(fos, "TURTLE");
			fos.flush();
			fos.close();
			uri = file.toURI().toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return uri != null ? uri : "";
	}

}
