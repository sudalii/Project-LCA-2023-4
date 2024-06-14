package kr.re.ImportTest2.component.result;

import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.component.result.resultTable.ProcessResultTable;
import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.service.CalcResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openlca.core.database.*;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.matrix.cache.FlowTable;
import org.openlca.core.matrix.index.TechFlow;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.model.descriptors.ImpactDescriptor;
import org.openlca.core.results.EnviFlowValue;
import org.openlca.core.results.LcaResult;
import org.openlca.core.results.TechFlowValue;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class Calculation {

    private static IDatabase db = null;

    public LcaResult calculate(String methodName, ProductSystem system, Process process, boolean removeFlag) {
        db = SystemBuilder.db;
        validateSystem(system);
        var method = new ImpactMethodDao(db).getForName(methodName).get(0);
        log.info("method find: {}", method);
        for (Process p : new ProcessDao(db).getForIds(system.processes)) {
            try {
                if (p.name.equals(system.name)) {
                    log.info("P: {} {} {}", p.name, p.id, p.refId);
//                if (p.name.equals(system.name)) {
                    CalculationSetup setup = CalculationSetup.of(p).withImpactMethod(method);
                    SystemCalculator calc = new SystemCalculator(db);
                    log.info("calc: {}", calc);
                    LcaResult result = calc.calculate(setup);
                    log.info("result: {}", result);
                    if (result != null)
                        return result;
                }
//                }
            } catch (Exception e) {
                log.warn("Exception for process {}: {}", p.name, e.getMessage());
                // Optionally log the stack trace if needed
                // log.warn("Stack trace: ", e);
            }
        }
        if (removeFlag) {
            List<Process> ps = new ProcessDao(db).getForIds(system.processes);
            new ProcessDao(db).deleteAll(ps);
            new ProductSystemDao(db).deleteAll();
            closeDb();
        }
        return null;
    }

    private void validateSystem(ProductSystem system) {
        log.info("validateSystem start");
        log.info("System info: {}", system);
        List<Process> ps = new ProcessDao(db).getForIds(system.processes);
        for (Process p : ps) {
            log.info("{}, {}, {}, {}", p.name, p.id, p.refId, p.exchanges.size());
            for (Exchange e : p.exchanges) {
                Process provider = new ProcessDao(db).getForId(e.defaultProviderId);
//                if (p.name.equals(system.name)) {
                    log.info("  Exchange: {} ({}), amount: {}{} -> Provider: {}", e.flow.name,
                            e.flow.flowType, e.amount, e.unit.name, provider != null ? provider.name : "null");
                    if (e.amount == 0){
                        log.info("  --> find! amount is 0");
                    }
//                }
            }
        }
//        log.info("FlowTable type: {}", FlowTable.getTypes(db));
//        log.info("FlowTable directionOf: {}", FlowTable.directionsOf(db, ));
    }

    /**
     * impact -> impact factor 추출 -> techFlow(provider=공정별) -> enviFlow * factor(flow 별)
     */
    public Pair<CategoryResultTable, List<FlowResultTable>> getResultOne(LcaResult r, String saveCgName) {
        log.info("");
        log.info("Category Result Method");
        CategoryResultTable cgTable = null;
        List<FlowResultTable> flowTables = null;
        for (int i = 0; i < r.impactIndex().size(); i++) {
            ImpactDescriptor impactDesc = r.impactIndex().at(i);
            String cgName = impactDesc.name;
            if (cgName.equals(saveCgName)) {
                log.info("category name = {}", cgName);
                ImpactDescriptor impact = r.impactIndex().at(i);
                double totalImpactValue = r.getTotalImpactValueOf(impact); // 총 카테고리 결과값

                log.info("{} -> {} {}", impactDesc.name,
                        String.format("%.6f", totalImpactValue), impactDesc.referenceUnit);

                // 가장 많은 양의 flow top 3
                List<EnviFlowValue> flowImpactsOf = r.getFlowImpactsOf(impact);
                log.info("가장 많은 양의 flow top 3:");
                flowTables = FlowResultTable.convertToFlowTables(flowImpactsOf, r, impact);
                log.info("flowTables: {}", flowTables);

                List<TechFlowValue> processResults = r.getTotalImpactValuesOf(impact)
                        .stream()
                        .sorted(Comparator.comparingDouble(TechFlowValue::value).reversed())
                        .toList();
                List<ProcessResultTable> pTables = processResult(r, impact, processResults);
                cgTable = new CategoryResultTable(cgName, totalImpactValue, impact.referenceUnit, pTables);
                break;
            }
        }
        r.dispose();  // clean up
        return Pair.of(cgTable, flowTables);
    }

    /**
     * For API
     */
    public CategoryResultTable categoryResultForApi(LcaResult r) {
        log.info("");
        log.info("Category Result Method");
        CategoryResultTable cgTable = null;
        for (int i = 0; i < r.impactIndex().size(); i++) {
            ImpactDescriptor impactDesc = r.impactIndex().at(i);
            String cgName = impactDesc.name;
            switch (cgName) {
                case "Global warming (GWP100a)":
                case "Abiotic depletion":
                case "Human toxicity":
                case "Eutrophication":
                case "Water use":
                    log.info("category name = {}", cgName);
                    ImpactDescriptor impact = r.impactIndex().at(i);
                    double totalImpactValue = r.getTotalImpactValueOf(impact); // 총 카테고리 결과값

                    log.info("{} -> {} {}", impactDesc.name,
                            String.format("%.6f", totalImpactValue), impactDesc.referenceUnit);

                    List<TechFlowValue> processResults = r.getTotalImpactValuesOf(impact)
                            .stream()
                            .sorted(Comparator.comparingDouble(TechFlowValue::value).reversed())
                            .toList();
                    List<ProcessResultTable> pTables = processResult(r, impact, processResults);
                    cgTable = new CategoryResultTable(cgName, totalImpactValue, impact.referenceUnit, pTables);
                    break;
            }
        }
        r.dispose();  // clean up
        return cgTable;
    }

    public List<CategoryResultTable> categoryAllResultForApi(LcaResult r) {
        log.info("");
        log.info("Category All Result Method");
        List<CategoryResultTable> cgTables = new ArrayList<>();
        CategoryResultTable cgTable;
        for (int i = 0; i < r.impactIndex().size(); i++) {
            String cgName = r.impactIndex().at(i).name;
            switch (cgName) {
                case "Global warming (GWP100a)":
                case "Abiotic depletion":
                case "Human toxicity":
                case "Eutrophication":
                case "Water use":
                    log.info("");
                    log.info("category name = {}", cgName);
                    ImpactDescriptor impact = r.impactIndex().at(i);
                    double totalImpactValue = r.getTotalImpactValueOf(impact); // 총 카테고리 결과값

                    List<TechFlowValue> processResults = r.getTotalImpactValuesOf(impact)
                            .stream()
                            .sorted(Comparator.comparingDouble(TechFlowValue::value).reversed())
                            .toList();
                    List<ProcessResultTable> pTables = processResult(r, impact, processResults);
                    cgTable = new CategoryResultTable(cgName, totalImpactValue, impact.referenceUnit, pTables);
                    cgTables.add(cgTable);
            }
        }
        r.dispose();  // clean up
        return cgTables;
    }

    /**
     * Process 단위의 Result 값 추출
     */
    private List<ProcessResultTable> processResult(LcaResult r, ImpactDescriptor impact, List<TechFlowValue> processResults) {
        log.info("Process별 결과값 추출");
        List<ProcessResultTable> processResultTables = new ArrayList<>();
        ProcessResultTable pTable;

        for (TechFlowValue processResult : processResults) {
            if (!Objects.equals(processResult.flow().name, SystemBuilder.name)) {  // 어디서 가져오기. Productsystem name으로 ?
                log.info("");
                log.info("{} = {}, {} = {}, value = {}",
                        processResult.provider().type, processResult.provider().name,
                        processResult.flow().flowType, processResult.flow().name, String.format("%.4f", processResult.value()));
                if (processResult.value() == 0) {
                    log.info("processResult.value is close 0 -> pass");
                    continue;
                }
                List<FlowResultTable> flowResultTables = flowResult(r, impact, processResult);

                pTable = new ProcessResultTable(processResult.provider().name, processResult.value(), flowResultTables);
                processResultTables.add(pTable);
            }
        }
        return processResultTables;
    }

    /**
     * Flow 단위의 Result 값 추출
     */
    private List<FlowResultTable> flowResult(LcaResult r, ImpactDescriptor impact, TechFlowValue processResult) {
        log.info("Flow별 결과값 추출");
        List<FlowResultTable> flowResultTables = new ArrayList<>();
        FlowResultTable fTable;
        TechFlow techFlow = processResult.techFlow();
        int n = 1;
        double add = 0.0;
        // Flow 단위의 Result 값 추출
        for (EnviFlowValue efv : r.getTotalFlowsOf(techFlow)) { // efv = Inventory result(kg), 원래 flow 값*공정양 임.
            double value = r.getImpactFactorOf(impact, efv.enviFlow());
            if ((value > 0) && (efv.value() != 0)) {
                double factorOf = r.getImpactFactorOf(impact, efv.enviFlow());
                double impactResult = efv.value() * factorOf;
                fTable = new FlowResultTable(
                        efv.flow().name, efv.value(), factorOf, impactResult);
                log.info("name: {}", fTable.name());
                // lciResult * factor = flowImpactResult
                log.info("--> lciResult = {}, flowImpactResult = {}", fTable.lciResult(), fTable.impactResult());
                n++;
                add = add + efv.value() * factorOf;
                flowResultTables.add(fTable);
            }
        }
        log.info("{}개, flowResult 합산: {}", n, add);
        log.info("");
        return FlowResultTable.sortByImpactResultDescending(flowResultTables);
    }

    private void closeDb() {
        try {
            SystemBuilder.closeDb();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        db = null;
    }

    public void getResultTables(List<CategoryResultTable> cgResultTables) {
        // By Category
        for (CategoryResultTable cgTable : cgResultTables) {
            log.info("Category별 결과값 추출 확인, 총 {}개", cgResultTables.size());
            log.info("{} -> {} {}", cgTable.name(),
                    String.format("%.6f", cgTable.value()), cgTable.unit());

            // By Process
            List<ProcessResultTable> pTables = cgTable.processResults();
            log.info("\tProcess별 결과값 추출 확인, 총 {}개", pTables.size());
            for (ProcessResultTable pTable : pTables) {
                log.info("\t{} -> {}", pTable.name(), pTable.value());

                // By Flow
                List<FlowResultTable> fTables = pTable.flowResults();
                log.info("\t\tFlow별 결과값 추출 확인, 총 {}개", fTables.size());
                for (FlowResultTable fTable : fTables) {
                    log.info("\t\t{} -> lciResult = {}, flowImpactResult = {}",  // lciResult * factor = flowImpactResult
                            fTable.name(), fTable.lciResult(), fTable.impactResult());
                }
            }
            log.info("");
        }
    }

    public void saveResultTables(List<CategoryResultTable> cgResultTables) {

//        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        String home = System.getProperty("user.home");
        String prePath = home + "/server/txt";
        File file = new File(prePath);
        if (file.mkdirs())
            log.info("create /server/txt/ directory.");

        try (PrintWriter writer = new PrintWriter(prePath+"/resultTables.txt")) {
            // By Category
            for (CategoryResultTable cgTable : cgResultTables) {
                writer.println("Category별 결과값 추출 확인, 총 " + cgResultTables.size() + "개");
                writer.println(cgTable.name() + " -> " + String.format("%.4f", cgTable.value()) + cgTable.unit());

                // By Process
                List<ProcessResultTable> pTables = cgTable.processResults();
                writer.println("\tProcess별 결과값 추출 확인, 총 " + pTables.size() + "개");
                for (ProcessResultTable pTable : pTables) {
                    writer.println("\t" + pTable.name() + " -> " + String.format("%.4f", pTable.value()));

                    // By Flow
                    List<FlowResultTable> fTables = pTable.flowResults();
                    writer.println("\t\tFlow별 결과값 추출 확인, 총 " + fTables.size() + "개");
                    for (FlowResultTable fTable : fTables) {
                        writer.println("\t\t" + fTable.name() + " -> " + "lciResult = "
                                + fTable.lciResult() + ", flowImpactResult = " + String.format("%.4f", fTable.impactResult()));
                    }
                }
                writer.println("");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
