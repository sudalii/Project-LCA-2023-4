package org.earthster.client.rdf;

import org.earthster.client.model.ImpactCategory;
import org.earthster.client.rdf.vocabulary.Impact2002;
import org.earthster.client.rdf.vocabulary.Units;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * An enumeration with constants for the de-/serialisation of LCIA categories.
 */
public enum ImpactCategoryTerm {

	ClimateChange("5e952c5c-38e7-4f65-bbea-34565f9331d0", "Climate change",
			"kg CO2 eq", Impact2002.cdClimateChange, Units.kgCO2Equiv),

	EcoSystemQuality("58e06363-ac2b-40c5-b47b-478d628219f7",
			"Ecosystem quality", "PDF*m2*yr", Impact2002.cdEcosystemQuality,
			Units.pdfM2Year),

	HumanHealt("6040828c-d79d-4a29-bc80-65d5775de3c2", "Human health", "DALY",
			Impact2002.cdHumanHealth, Units.daly),

	Resources("a91f36b1-7538-4a71-8668-c16fec2113f1", "Resources",
			"MJ primary", Impact2002.cdResources, Units.MJPrimaryNRE);

	private final String uuid;
	private final String name;
	private final String unit;
	private final Resource categoryResource;
	private final Resource unitResource;

	private ImpactCategoryTerm(String uuid, String name, String unit,
			Resource categoryResource, Resource unitResource) {
		this.uuid = uuid;
		this.name = name;
		this.unit = unit;
		this.categoryResource = categoryResource;
		this.unitResource = unitResource;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Converts the term to an instance of LCIA category.
	 */
	public ImpactCategory getCategory() {
		ImpactCategory category = new ImpactCategory();
		category.setId(uuid);
		category.setName(name);
		category.setUnit(unit);
		return category;
	}

	/**
	 * Returns the term for the given resource or null if there is no term for
	 * this resource.
	 */
	public static ImpactCategoryTerm fromResource(final Resource resource) {
		Filter filter = new Filter() {
			@Override
			boolean match(ImpactCategoryTerm candidate) {
				return candidate.categoryResource.equals(resource);
			}
		};
		return filter.getResult();
	}

	/**
	 * Returns the term for the given category or null if there is no term for
	 * this category.
	 */
	public static ImpactCategoryTerm fromCategory(final ImpactCategory category) {
		Filter filter = new Filter() {
			@Override
			boolean match(ImpactCategoryTerm candidate) {
				return candidate.uuid.equals(category.getId());
			}
		};
		return filter.getResult();
	}

	/**
	 * Get the RDF resource of the LCIA category of the term.
	 */
	public Resource getCategoryResource() {
		return categoryResource;
	}

	/**
	 * Get the RDF resource of the LCIA unit of the term.
	 */
	public Resource getUnitResource() {
		return unitResource;
	}

	/**
	 * A method template for filtering terms.
	 */
	private static abstract class Filter {

		abstract boolean match(ImpactCategoryTerm term);

		ImpactCategoryTerm getResult() {
			ImpactCategoryTerm result = null;
			ImpactCategoryTerm[] terms = values();
			int n = terms.length;
			int i = 0;
			while (result == null && i < n) {
				ImpactCategoryTerm candidate = terms[i];
				if (match(candidate)) {
					result = candidate;
				}
				i++;
			}
			return result;
		}
	}

}
