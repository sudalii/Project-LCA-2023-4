package examples;

import org.openlca.app.tools.openepd.input.ImportDialog;
import org.openlca.app.util.MsgBox;
import org.openlca.app.wizards.io.DbImportPage;
import org.openlca.core.DataDir;
import org.openlca.core.database.Derby;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ImpactMethodDao;
import org.openlca.core.database.upgrades.VersionState;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.io.Format;
import org.openlca.io.openepd.EpdDoc;
import org.openlca.jsonld.Json;
import org.openlca.jsonld.ZipStore;
import org.openlca.jsonld.output.JsonExport;
import org.openlca.nativelib.NativeLib;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.*;


public class MyCalculationExample0711 {

	private final DbImportPage.ImportConfig config;	// Page구현은 없어야 하므로 삭제예정
	private File tempDbFolder;
	private IDatabase source;

	ConnectionDispatch(DbImportPage.ImportConfig config) {
		this.config = config;
	}

	public IDatabase getSource() {
		return source;
	}

	public VersionState getSourceState() {
		return VersionState.get(source);
	}

	private IDatabase connectToFolder() {
		File zipFile = config.file;
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		tempDbFolder = new File(tempDir, UUID.randomUUID().toString());
		tempDbFolder.mkdirs();
		log.trace("unpack zolca file to {}", tempDbFolder);
		ZipUtil.unpack(zipFile, tempDbFolder);
		return new Derby(tempDbFolder);
	}

	private static void importFile() {
		String path = "../../resources/dataset/openLCA_IW_plus_for_ei3_5";
		var file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			MsgBox.error("Not a file", path + " is not an existing file.");
			return;
		}

		// check if it is a known import format
		var format = Format.detect(file).orElse(null);
		if (format != null) {
			handleFormat(file, format);
			return;
		}

		// check if it is an openEPD file
		var name = file.getName().toLowerCase();
		if (name.endsWith(".json")) {
			try {
				var json = Json.read(file).orElse(null);
				if (json != null && json.isJsonObject()) {
					var obj = json.getAsJsonObject();
					if (Objects.equals("OpenEPD", Json.getString(obj, "doctype"))) {
						var epd = EpdDoc.fromJson(obj).orElse(null);
						if (epd != null) {
							ImportDialog.show(epd);
							return;
						}
					}
				}
			} catch (Exception ignored) {
			}
		}

		MsgBox.info("Unknown format",
			"openLCA could not detect the format of the file '"
				+ file.getName() + "'. You can also try an "
				+ "import option in the generic import dialog "
				+ "under Import > Other");

	}

	private static void handleFormat(File file, Format format) {
	}

	/**
	 * ref: test code
	 */
	public void testDetectJSONLD() throws Exception {
		var db = Tests.getDb();
		var units = db.insert(UnitGroup.of("Mass units", Unit.of("kg")));
		var mass = db.insert(FlowProperty.of("Mass", units));
		var steel = db.insert(Flow.product("Steel", mass));
		var process = db.insert(Process.of("Steel production", steel));

		var file = Files.createTempFile("_olca_test", ".zip").toFile();
		Files.delete(file.toPath());
		try (var zip = ZipStore.open(file)) {
			new JsonExport(db, zip).write(process);
		}
		check(file, Format.JSON_LD_ZIP);
		db.clear();
	}

	public static void main(String[] args) {

		double TARGET_AMOUNT = 1.28;

		/*************************** 1. DB load  ***************************/

		NativeLib.loadFrom(DataDir.get().root());

//		try (var db = DataDir.get().openDatabase("test1012_copy")) {
		// openDatabase() -> Derby(): DB 있으면 그대로 호출, 없으면 새로 생성까지 가능
		try (var db = DataDir.get().openDatabase("test_0711")) {

				/***************** Process load *****************/
				var system = db.get(ProductSystem.class,
					"80abdce2-7d8b-455c-a48d-3d82cea05aed");

				//var method = new ImpactMethodDao(db)
				//	.getForRefId("effb055a-ad78-39bd-8dc0-341411db4ae7");
				var method = new ImpactMethodDao(db).getForName("CML-IA baseline").get(0);

				system.targetAmount = TARGET_AMOUNT;
				System.out.println("method:" + method + method.getClass().getName());

				var setup = CalculationSetup.fullAnalysis(system)
					.withImpactMethod(method);
				//var setup = CalculationSetup.simple(system)
				//		.withImpactMethod(method);
				System.out.printf("%s %s%s / %s\n\n",
					system.name, system.targetAmount, system.targetUnit.name, method.name);
				var calc = new SystemCalculator(db);
				var r = calc.calculateFull(setup);
				//var r = calc.calculateSimple(setup);

				//fs.getTotalFlowResults();

				int i=0;
				HashMap<String, Double> fs = new HashMap<>();

				for (i=0; i<r.enviIndex().size(); i++) {
					var f = r.enviIndex().at(i);
					double flowResult = r.getTotalFlowResult(f);
					fs.put(f.flow().name, flowResult);
					//if (r.getTotalFlowResult(f) > 0.000001) {
					// System.out.println(f.flow().name + "  -> " + r.getTotalFlowResult(f));
					//}
				}
				// 내림차순 정렬
				List<String> listKeySet = new ArrayList<>(fs.keySet());
				Collections.sort(listKeySet, (value1, value2) ->
					(fs.get(value2).compareTo(fs.get(value1))));

				System.out.println("The total inventory result of the given flow - Top 10:");
				int idx = 0;
				for(String key : listKeySet) {
					if (idx < 10) {
						System.out.printf("%s  -> %.8f\n", key, fs.get(key));
					}
					idx++;
				}
			/*
			for (i=0; i<r.impactIndex().size(); i++) {
				var impact = r.impactIndex().at(i);
				System.out.println("\nthe impact category results for the given result:");
				System.out.printf("%s : %.6f\n", impact.name, r.getTotalImpactResult(impact));
			}
			*/
				//System.out.println(f.flow().name + "  -> " + r.getTotalFlowResult(f));
				// System.out.println("r leng:"+r.impactIndex().size());

				i=0;
				while (i < r.impactIndex().size()) {
					if (r.impactIndex().at(i).toString().contains("GWP")) {
						var impact =  r.impactIndex().at(i);
						//System.out.println(impact.name + "  -> " + );
						System.out.printf("%n%s  -> %.6f %s%n%n",
							impact.name, r.getTotalImpactResult(impact), impact.referenceUnit);
						break;
					}
					i++;
				}


			}
		}
	}
