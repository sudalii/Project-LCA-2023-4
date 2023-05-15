package org.earthster.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/** A process or service with a set of input and outputs. */
@Entity
public class Activity implements Copyable<Activity> {

	@Id
	private String id;
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_ACTIVITY")
	private List<Exchange> exchanges = new ArrayList<Exchange>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Exchange> getExchanges() {
		return exchanges;
	}

	@Override
	public Activity copy() {
		Activity copy = new Activity();
		copy.id = UUID.randomUUID().toString();
		copy.name = this.name;
		for (Exchange exchange : this.exchanges) {
			Exchange c = exchange.copy();
			copy.exchanges.add(c);
		}
		return copy;
	}

}
