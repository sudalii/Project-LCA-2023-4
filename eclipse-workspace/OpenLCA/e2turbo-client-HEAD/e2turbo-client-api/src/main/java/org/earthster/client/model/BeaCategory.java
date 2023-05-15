package org.earthster.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class BeaCategory {

	@Id
	private String id;

	private String name;

	@OneToMany
	@JoinColumn(name = "FK_BEA_CATEGORY")
	private List<BeaCommodity> commodities = new ArrayList<BeaCommodity>();

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

	public List<BeaCommodity> getCommodities() {
		return commodities;
	}

}
