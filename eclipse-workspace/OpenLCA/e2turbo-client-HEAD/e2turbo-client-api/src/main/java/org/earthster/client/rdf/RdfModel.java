package org.earthster.client.rdf;

import org.earthster.client.rdf.vocabulary.DBPedia;
import org.earthster.client.rdf.vocabulary.DublinCore;
import org.earthster.client.rdf.vocabulary.ECO;
import org.earthster.client.rdf.vocabulary.EPSResource;
import org.earthster.client.rdf.vocabulary.GoodRelations;
import org.earthster.client.rdf.vocabulary.Impact2002;
import org.earthster.client.rdf.vocabulary.RDFS;
import org.earthster.client.rdf.vocabulary.Units;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * A helper class for the creation of RDF models and resources.
 */
public class RdfModel {

	private RdfModel() {
	}

	/**
	 * Create a new RDF model.
	 */
	public static Model createModel() {
		Model model = ModelFactory.createDefaultModel();
		addPrefixes(model);
		return model;
	}

	/**
	 * Adds the name space prefixes to the model.
	 */
	private static void addPrefixes(Model model) {
		model.setNsPrefix(EPSResource.PREFIX, EPSResource.NS);
		model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		model.setNsPrefix(DBPedia.PREFIX, DBPedia.NS);
		model.setNsPrefix(DublinCore.PREFIX, DublinCore.NS);
		model.setNsPrefix(ECO.PREFIX, ECO.NS);
		model.setNsPrefix(GoodRelations.PREFIX, GoodRelations.NS);
		model.setNsPrefix(Impact2002.PREFIX, Impact2002.NS);
		model.setNsPrefix(RDFS.PREFIX, RDFS.NS);
		model.setNsPrefix(Units.PREFIX, Units.NS);
	}

}
