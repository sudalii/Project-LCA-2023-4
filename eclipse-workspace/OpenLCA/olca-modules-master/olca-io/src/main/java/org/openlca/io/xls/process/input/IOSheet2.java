package org.openlca.io.xls.process.input;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.database.CategoryDao;
import org.openlca.core.database.FlowDao;
import org.openlca.core.io.CategorySync;
import org.openlca.core.io.DbEntityResolver;
import org.openlca.core.model.*;
import org.openlca.io.maps.FlowSync;
import org.openlca.io.maps.SyncFlow;
import org.openlca.util.Categories;
import org.openlca.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class IOSheet2 {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Config config;
	private final boolean forInputs;
	private final Sheet sheet;

	private IOSheet2(Config config, boolean forInputs) {
		this.config = config;
		this.forInputs = forInputs;
		// String sheetName = "투입물 및 산출물";
		// sheet = config.workbook.getSheet(sheetName);	// sheet name 수정
		sheet = getSheetName(config);
	}

	/**
	 * @param config
	 * @return "투입물 및 산출물" or "투입물 및 산출물 (CtG)"
	 */
	private Sheet getSheetName(Config config){
		return config.workbook.getSheetAt(3); // sheet 위치로 지정 (이름 달라서)
	}

	/**
	 * Input / Output을 Boolean으로 구분 x,
	 * column 1의 string에 따라 판별하도록 구현 (INPUT / OUTPUT)
	 */
	public static void readInputs(Config config) {
		new IOSheet2(config, true).read();
	}

	public static void readOutputs(Config config) {
		new IOSheet2(config, false).read();
	}

	private void read() {
		if (sheet == null) {
			return;
		}
		try {
			log.trace("read exchanges; inputs={}", forInputs);
			int row = 1;
			while (true) {
				Exchange exchange = readExchange(row);
				if (exchange == null) {
					break;
				}
				row++;
			}
		} catch (Exception e) {
			log.error("failed to read exchanges", e);
		}
	}

	/**
	 * read()에서 row 1씩 증가할 때마다 call
	 */
	private Exchange readExchange(int row) {
		RefData refData = config.refData;
		String name = config.getString(sheet, row, 4);	// Flow 이름
		if (name == null) {
			return null;
		}
		// 원래 return 형태 String: Elementary flows/Resource/in ground
		var category = getCategory(name, refData, row);	// 여기서 카테고리 없으면 생성까지 해야함
																						// 아니면 밑 if절에 걸림

		String unitName = config.getString(sheet, row, 7);
		Unit unit = refData.getUnit(unitName);
		if (unit == null) {
			return refDataError(row, "unit: " + unitName);
		}

		var flow = getFlow(name, refData, category);
//		if (flow == null) {
//			return refDataError(row, "flow: " + name + "/" + category.name);
//		}

		String propName = config.getString(sheet, row, 2);
		FlowProperty property = refData.getFlowProperty(propName);
		if (property == null) {
			return refDataError(row, "flow property: " + propName);
		}
		if (flow.getFactor(property) == null) {
			return refDataError(row, "flow property factor: " + propName);
		}

		var exchange = config.process.add(Exchange.of(flow, property, unit));
		exchange.isInput = forInputs;
		exchange.amount = config.getDouble(sheet, row, 10);
		String formula = config.getString(sheet, row, 8);	// 단위 - 설명
		if (!Strings.nullOrEmpty(formula)) {
			exchange.formula = formula;
		}

		return exchange;
	}
	private Category getCategory(String flowName, RefData refData, int row) {
//		String name = config.getString(sheet, row, 4);	// Flow 이름

		// Flow 카테고리 찾기
		String s1 = config.getString(sheet, row, 2);
		String s2 = config.getString(sheet, row, 3);

		CategorySync categorySync = CategorySync.of(db);
		Category category = categorySync.get(ModelType.forModelClass(Category.class), s1, s2);

		return category;
	}
	private Flow getFlow(String flowName, RefData refData, Category category) {
		var flow = refData.getFlow(flowName, category.name);	// 원래 var flow

		if (flow == null) {
			return createFlow(flowName, category.name);
		}
		return flow;
	}
	private void readFlows() {
		int row = 1;
		while (true) {
			String uuid = config.getString(flowSheet, row, 0);
			if (com.google.common.base.Strings.isNullOrEmpty(uuid)) {
				break;
			}
			Flow flow = dao.getForRefId(uuid);
			if (flow != null) {
				syncFlow(row, flow);
			} else {
				createFlow(row, uuid);
			}
			row++;
		}
	}

	private void syncFlow(int row, Flow flow) {
		String name = config.getString(flowSheet, row, 1);
		String category = config.getString(flowSheet, row, 3);
		List<FlowSheets.Factor> factors = this.factors.get(key(name, category));
		if (factors == null || flow.referenceFlowProperty == null) {
			config.refData.putFlow(name, category, flow);
			return;
		}
		String refProperty = config.getString(flowSheet, row, 10);
		if (!Objects.equals(refProperty, flow.referenceFlowProperty.name)) {
			// cannot add more factors as the reference flow property is not
			// the same
			config.refData.putFlow(name, category, flow);
			return;
		}
		boolean updated = addFactors(flow, factors);
		if (updated) {
			flow.lastChange = Calendar.getInstance().getTimeInMillis();
			Version.incUpdate(flow);
			flow = dao.update(flow);
		}
		config.refData.putFlow(name, category, flow);
	}

	private Flow createFlow(String flowName, String category) {
		Flow flow = new Flow();
		flow.refId = UUID.randomUUID().toString();
		flow.name = flowName;
		flow.category = config.getCategory(category, ModelType.FLOW);

//		if (category.contains("Product")) {
//			flow.flowType = FlowType.PRODUCT_FLOW;
//		} else {
//			flow.flowType = FlowType.ELEMENTARY_FLOW;
//		}
		flow.flowType = category.contains("Product")
			? FlowType.PRODUCT_FLOW
			: FlowType.ELEMENTARY_FLOW;

		flow.referenceFlowProperty = flowProperty;
		var factor = new FlowPropertyFactor();
		factor.conversionFactor = 1;
//		factor.flowProperty = flowProperty;
		flow.flowPropertyFactors.add(factor);

//		var property = property(unit);
//		Flow flow = Flow.of(name, type, property);
		new FlowDao(db).insert(flow);
		config.refData.putFlow(flowName, category, flow);

		return flow;
	}

	private void createFlow2(String name, String category) {
		Flow flow = new Flow();
		flow.refId = UUID.randomUUID().toString();
		flow.name = name;
		flow.category = config.getCategory(category, ModelType.FLOW);
//		setAttributes(row, flow);
		List<FlowSheets.Factor> factors = this.factors.get(key(name, category));
		if (factors == null) {
			log.error("could not create flow {}/{}; no flow property factors",
				name, category);
			return;
		}
		createPropertyFactors(row, flow, factors);
		flow = dao.insert(flow);
		config.refData.putFlow(name, category, flow);
	}

	// 삭제 예정
	private Flow flow(String name, String unit, FlowType type) {
		FlowDao dao = new FlowDao(db);
		List<Flow> flows = dao.getForName(name);
		if (!flows.isEmpty())
			return flows.get(0);
		var property = property(unit);
		Flow flow = Flow.of(name, type, property);
		return dao.insert(flow);
	}

	private Exchange refDataError(int row, String message) {
		log.error("could not create an exchange because of missing reference "
				+ "datum: {}; forInputs={} row={}", message, forInputs, row);
		return null;
	}

}
