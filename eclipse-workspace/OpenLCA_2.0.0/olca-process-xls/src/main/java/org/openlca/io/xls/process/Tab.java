package org.openlca.io.xls.process;

/**
 * A tab (sheet) in a process workbook.
 */
enum Tab {

	ACTORS("Actors"),
	ADMINISTRATIVE_INFORMATION("Administrative information"),
	ALLOCATION("Allocation"),
	CURRENCIES("Currencies"),
	FLOW_PROPERTIES("Flow properties"),
	FLOW_PROPERTY_FACTORS("Flow property factors"),
	FLOWS("Flows"),
	GENERAL_INFO("General information"),
	INPUTS("Inputs"),
	LOCATIONS("Locations"),
	MODELING_AND_VALIDATION("Modeling and validation"),
	OUTPUTS("Outputs"),
	PARAMETERS("Parameters"),
	PROVIDERS("Providers"),
	SOURCES("Sources"),
	UNIT_GROUPS("Unit groups"),
	UNITS("Units");

	private final String label;

	Tab(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
