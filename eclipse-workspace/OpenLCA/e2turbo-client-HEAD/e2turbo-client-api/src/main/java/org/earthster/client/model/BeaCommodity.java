package org.earthster.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class BeaCommodity {

	@Id
	private String code;

	private String name;

	private String productId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_BEA_COMMODITY")
	private List<BeaItem> items = new ArrayList<BeaItem>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BeaItem> getItems() {
		return items;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
