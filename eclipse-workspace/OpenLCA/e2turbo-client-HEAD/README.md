The Earthster Client API 
=======================
This repository contains some of the non-ui functions of the [Earthster 2 Turbo
client tool](http://www.greendeltatc.com/Earthster-2-Turbo.200.0.html?&L=1).
The API currently includes the following components:

* A simple domain model for Economic Input-Output Life Cycle Assessment (EIO-LCA)
* An embedded database and search index based on [Apache Derby]
  (http://db.apache.org/derby/) and [Apache Lucene](http://lucene.apache.org/core/)
* A RDF import and export module using [Apache Jena](http://jena.apache.org/)

Model and Database
------------------
The model and database API provide an easy way to create activity data sets
and store them in a database:

		Database db = new Database("path/to/database/dir");

		// create a new activity
		Activity activity = new Activity();
		activity.setId("activity-id");
		activity.setName("my activity");
		db.insert(activity);

		// create a new product and link it to the activity
		Product product = new Product();
		product.setActivityId("my-activity-id");
		product.setId("product-id");
		product.setName("my product");
		db.insert(product);

		// create a flow
		Flow flow = new Flow();
		flow.setId("flow-id");
		flow.setName("CO2");
		flow.setType(Flow.ELEM_FLOW);
		flow.setUnit("kg");
		db.insert(flow);

		// set the flow as output of the activity
		Exchange exchange = new Exchange();
		exchange.setId("exchange-1");
		exchange.setFlow(flow);
		exchange.setAmount(5);
		exchange.setInput(false);
		activity.getExchanges().add(exchange);
		db.update(activity);

		// read it from the database
		Activity a = db.select(Activity.class, "activity-id");
		assert a.equals(activity);
		assert a.getExchanges().get(0).getFlow().equals(flow);

		// delete it from the database
		db.delete(activity);
		db.delete(product);
		db.delete(flow);

RDF Import and Export
---------------------
The client API also contains some features to import and export product
definitions and impact assessment results in RDF format. Here is an example 
how to use the API:

		// create a simple LCIA result
		Assessment assessment = new Assessment();
		assessment.setId("id-of-LCIA-result");
		AssessmentResult r1 = new AssessmentResult();
		r1.setCategory(ImpactCategoryTerm.ClimateChange.getCategory());
		r1.setId("r1");
		r1.setValue(42);
		assessment.getResults().add(r1);

		// write it to a RDF model
		Model model = ModelFactory.createDefaultModel();
		RdfAssessmentWriter writer = new RdfAssessmentWriter(model);
		writer.write(assessment);

		// print it on the console
		model.write(System.out, "TURTLE");

This will print the following output on the console:

		<http://eps.earthster.org/eps/impactassessment/uuid/id-of-LCIA-result>
      a       <http://ontology.earthster.org/eco/core#ImpactAssessment> , <http://ontology.earthster.org/eco/impact2002Plus#ImpactAssessment> ;
      <http://ontology.earthster.org/eco/core#hasImpactAssessmentMethod>
              <http://ontology.earthster.org/eco/impact2002Plus#impact2002Plus> ;
      <http://ontology.earthster.org/eco/core#hasImpactCategoryIndicatorResult>
              [ <http://ontology.earthster.org/eco/core#hasImpactAssessmentMethodCategoryDescription>
                        <http://ontology.earthster.org/eco/impact2002Plus#cdClimateChange> ;
                <http://ontology.earthster.org/eco/core#hasQuantity>
                        [ <http://ontology.earthster.org/eco/core#hasMagnitude>
                                  "42.0"^^<http://www.w3.org/2001/XMLSchema#float> ;
                          <http://ontology.earthster.org/eco/core#hasUnitOfMeasure>
                                  <http://ontology.earthster.org/eco/unit#kgCO2Equiv>
                        ]
              ] ;
      <http://ontology.earthster.org/eco/core#hasUUID>
              "id-of-LCIA-result" ;
      <http://ontology.earthster.org/eco/core#quantityAssessed>
              [ <http://ontology.earthster.org/eco/core#hasMagnitude>
                        "1"^^<http://www.w3.org/2001/XMLSchema#int> ;
                <http://ontology.earthster.org/eco/core#hasUnitOfMeasure>
                        <http://dbpedia.org/resource/U.S._dollar>
              ] ;
      <http://purl.org/dc/elements/1.1/date>
              "2013-03-23"^^<http://www.w3.org/2001/XMLSchema#date> .

License
-------
This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.