package org.earthster.client.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A category of a LCIA method.
 */
@Entity
@Table(name = "LCIA_CATEGORY")
public class ImpactCategory {

	/**
	 * The id of the category.
	 */
	@Id
	private String id;

	/**
	 * The name of the category.
	 */
	private String name;

	/**
	 * The reference unit of the category.
	 */
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

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

}
