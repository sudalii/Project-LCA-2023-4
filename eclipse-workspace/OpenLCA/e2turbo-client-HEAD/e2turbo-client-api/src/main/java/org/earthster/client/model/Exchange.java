package org.earthster.client.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * An exchange is an input or output of a process (activity).
 */
@Entity
public class Exchange implements Copyable<Exchange> {

	@Id
	private String id;

	@Column(name = "IS_INPUT")
	private boolean input;

	private double amount;

	@OneToOne
	@JoinColumn(name = "FK_FLOW")
	private Flow flow;

	public boolean isInput() {
		return input;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	@Override
	public Exchange copy() {
		Exchange copy = new Exchange();
		copy.amount = this.amount;
		copy.flow = this.flow;
		copy.id = UUID.randomUUID().toString();
		copy.input = this.input;
		return copy;
	}

}
