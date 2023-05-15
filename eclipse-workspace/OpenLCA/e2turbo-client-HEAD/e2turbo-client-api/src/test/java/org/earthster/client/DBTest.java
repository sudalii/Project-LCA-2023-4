package org.earthster.client;

import org.earthster.client.database.Database;
import org.earthster.client.model.Activity;
import org.earthster.client.model.Exchange;
import org.earthster.client.model.Flow;
import org.earthster.client.model.Product;
import org.junit.Assert;
import org.junit.Test;

public class DBTest {

	@Test
	public void testConnect() {
		Database db = TestSession.getDatabase();
		Assert.assertTrue(db.isConnected());
		Assert.assertFalse(db.contains(Activity.class, "not exists"));
	}

	@Test
	public void testCrud() {
		Database db = TestSession.getDatabase();

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
		assert a.getExchanges().get(0).getFlow().getName().equals("CO2");

		// delete it from the database
		db.delete(activity);
		db.delete(product);
		db.delete(flow);
	}
}
