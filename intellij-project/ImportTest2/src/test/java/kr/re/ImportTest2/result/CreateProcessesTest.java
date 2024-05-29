package kr.re.ImportTest2.result;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.component.result.resultTable.ProcessResultTable;
import kr.re.ImportTest2.component.util.ConvertUnits;
import kr.re.ImportTest2.component.util.ConvertUnits.Converted;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.UserFlows;
import kr.re.ImportTest2.repository.SelectedProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openlca.core.database.*;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.index.EnviFlow;
import org.openlca.core.matrix.index.TechFlow;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.model.descriptors.ImpactDescriptor;
import org.openlca.core.results.EnviFlowValue;
import org.openlca.core.results.LcaResult;
import org.openlca.core.results.TechFlowValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
class CreateProcessesTest {

    private final RunDatabase runDatabase;
    private final SelectedProcessRepository spRepository;

    protected static IDatabase db = null;
    String productName = "ProductName01";
    double targetAmount = 50;
    String targetUnit = "t";

    @Autowired
    public CreateProcessesTest(RunDatabase runDatabase, SelectedProcessRepository spRepository) {
        this.runDatabase = runDatabase;
        this.spRepository = spRepository;
    }

    @BeforeEach
    void runDb() {
        db = runDatabase.runDb();
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
        } else {
            log.info("run db = {}", db.getName());
        }
    }

    @Test
    void run() {
        ConvertUnits.db = db;
        Process processes = createProcesses();

        // 3. 제품 정보를 가지고 만들어진 processes 안에 각 선택된 국가DB와 계산하여 process 추가
        List<SelectedProcess> allByUserId = spRepository.findAllByUserId(1L);

        int n = 1;
        for (SelectedProcess userP : allByUserId) {
            log.info("({}).", n);
            addProcess(processes, userP);
            n++;
        }
        Process updated = new ProcessDao(db).update(processes);

        // 4. 만들어진 Processes를 가지고 Product System 생성
        ProductSystem system = createProductSystem(updated);

        printLinkingStatus(system); // 추가: 링크 상태 출력

        LcaResult cml = calculate("CML-IA baseline", system, updated, false);
        LcaResult aware = calculate("AWARE", system, updated, true);

        List<CategoryResultTable> resultTables = categoryAllResult(cml);
        CategoryResultTable awareTable = categoryResult(aware);
        resultTables.add(awareTable);
        List<CategoryResultTable> sortedResultTables = CategoryResultTable.sortByImpactResultDescending(resultTables);

        saveResultTables(sortedResultTables);
    }

    Process createProcesses() {
        // baseFlow: Processes 생성을 위한 processes 기준흐름 가져오거나 생성
        Flow baseFlow = createProductBasedFlow();

        // 여러 process들을 flow로 받아서 product system으로 만들 process 가져오거나 생성
        Process processes = createProcess(baseFlow);

//        convertUnits.convertMassUnits(targetUnit);
        processes.quantitativeReference.unit = new UnitDao(db).getForName(targetUnit).get(0);
        processes.quantitativeReference.amount = targetAmount;

        return processes;
    }
    //    @Test
    Flow createProductBasedFlow() {
        // "이름"으로 검색해서 확인
//        new FlowDao(db).getForName(productName);
        List<Flow> searchF = new FlowDao(db).getForName(productName);
        if (searchF.isEmpty()) {
            log.info("{} flow is null, create this flow.", productName);
            FlowProperty mass = new FlowPropertyDao(db).getForName("Mass").get(0);
            Flow f = Flow.product(productName, mass);
            new FlowDao(db).insert(f);
        }
        Flow baseFlow = new FlowDao(db).getForName(productName).get(0);
        log.info("baseFlow = {}, flowType = {}", baseFlow, baseFlow.flowType);

        return baseFlow;
    }
    //    @Test
    Process createProcess(Flow baseFlow) {
        List<Process> searchP = new ProcessDao(db).getForName(productName);
        if (!searchP.isEmpty()) {
            for (Process p : searchP) {
                new ProcessDao(db).delete(p);
            }
        }
        log.info("{} processes is null, create this processes.", productName);
        Process p = Process.of(productName, baseFlow);
        new ProcessDao(db).insert(p);

        Process ps = new ProcessDao(db).getForName(productName).get(0);
        log.info("processes = {}, quantitativeReference = {}", ps, ps.quantitativeReference);

        return ps;
    }

    SelectedProcess getProcess() {
        SelectedProcess userP = spRepository.findById(7L).orElseThrow(
                () -> new RuntimeException("Not found userP"));

        log.info("userP name = {}, selectedDb = {}", userP.getProcessName(), userP.getMappedProcessId());

        return userP;
    }

    void addProcess(Process processes, SelectedProcess userP) {
//        SelectedProcess userP = getProcess();
//        printUnitList();
        // 사용자가 입력한 process name에 매칭된 process id으로
        // db(==국가DB)를 검색해서 가져오기
//        Process koreaDb = new ProcessDao(db).getForRefId("66601038-78de-4f7f-9c16-097572ee7cf0");
        Process koreaDb = new ProcessDao(db).getForRefId(userP.getMappedProcessId());
        Process customized = customizeAProcess(userP, koreaDb); // CustomizeLciDb()로 수정할까.


        // 해당 국가DB의 이름을 가진 product type flow 가져오기
        // import 시 자동 생성되어 get으로 가져오면 됨
        Exchange ref = customized.quantitativeReference;
        // processName 은... 일단 사용자 편의를 위해 프론트에서만 매칭하는 걸로 ?
        ref.amount = userP.getProcessAmount();
        ref.unit = new UnitDao(db).getForName(userP.getProcessAmountUnit()).get(0);
        ref.defaultProviderId = customized.id;
        processes.exchanges.add(ref);

        // 검증 코드
        for (Exchange ex: processes.exchanges) {
            if (ex.defaultProviderId == ref.defaultProviderId) {
                log.info("defaultProviderId: ref = {} {}, ex = {} {}\n",
                        ref.flow.name, ref.defaultProviderId, ex.flow.name, ex.defaultProviderId);
                Process validProvider = new ProcessDao(db).getForId(ex.defaultProviderId);
                Assertions.assertEquals(validProvider, customized);
                log.info("valid provider = {}, size = {}", validProvider.name, validProvider.exchanges.size());
                for (Exchange e : validProvider.exchanges) {
                    String eName = e.flow.name;
                    switch (eName) {
                        case "KR_Electricity":
                        case "Water, unspecified natural origin/kg":
                        case "Water (fresh water)":
                        case "Water/kg":
                            log.info("validate {} flow -> amount = {} {}", e.flow.name, e.amount, e.unit.name);
                        default:
                    }
                }
            }
        }
    }

    //    @Test
    Process customizeAProcess(SelectedProcess userP, Process koreaP) {
        log.info("CustomizeAProcess method start");
//        printUnitList();
        // 기존에 혹여나 있을 customize한 process는 지우고 시작
        List<Process> exist = new ProcessDao(db).getForName(userP.getProcessName());
        if (!exist.isEmpty()) {
            for (Process p : exist) {
                log.info("delete process of {}", p.name);
                new ProcessDao(db).delete(p);
            }
        }

        Process copy = koreaP.copy();
        copy.name = userP.getProcessName();

        // 배송은... 사실상 물질 단위에서 계산할 것이 없으므로.. 그냥 패스
        if (!getType(userP, "TRANSPORTATION")) {
            copy = calculateEnvironmentalLoad(userP, copy);
        } else {
            log.info("The userP type is TRANSPORTATION.");
        }
        
        // 확인코드
        Exchange ref = copy.quantitativeReference; // 국가DB의 기준물질 적재
        log.info("{}의 기준물질 = {}, {}{}\n", copy.name, ref.flow.name, ref.amount, ref.unit.name);

        setInputByType(userP, ref);

//        return new ProcessDao(db).insert(copy);
        return new ProcessDao(db).update(copy);
    }

    void setInputByType(SelectedProcess userP, Exchange ref) {
        if (getType(userP, "END_OF_LIFE")) {
            ref.isInput = false;
        } else {
            ref.isInput = true;
        }
    }

    /**
     * 계산 시 무조건 t->kg, MJ->kWh, m3->kg로 변환
     */
    Process calculateEnvironmentalLoad(SelectedProcess userP, Process copyKoreaP) {
        log.info("calculateEnvironmentalLoad method start");
        log.info("Custom LCI DB 계산식: 국가DB 물질 양 + (국가DB 공정 양(기준흐름) * 사용자입력 물질 값)/사용자입력 공정 값");
        UserFlows userF = userP.getFlows(); 
        ConvertUnits convert = new ConvertUnits();
        Converted userPAmount = convert.convertUnits(userP.getProcessAmount(), userP.getProcessAmountUnit());

        // 사용자 입력과 국가 DB 데이터를 기반으로 환경 부하 계산
        // 전기 = iFlow1, 물 = iFlow2 & oFlow1
        for (Exchange e : copyKoreaP.exchanges) {
            Flow f = e.flow;
            if ((e.isInput) && (userF.getIFlow2() != 0)) { // iWater 분별
                if (getType(userP, "RAW_MATERIALS")) {
                    if (f.name.equals("Water (fresh water)")) {
                        log.info("e.amount of {} = {}", f.name, e.amount);
                        Converted iWater = convert.convertUnits(userF.getIFlow2(), userF.getIFlow2Unit());
                        log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, iWater.value(), userPAmount.value());
                        e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * iWater.value()) / userPAmount.value();
                        log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                    }
                } else {    // No Raw_materials
                    if (f.name.equals("Water, unspecified natural origin/kg")) {
                        log.info("e.amount of {} = {}", f.name, e.amount);
                        Converted iWater = convert.convertUnits(userF.getIFlow2(), userF.getIFlow2Unit());
                        log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, iWater.value(), userPAmount.value());
                        e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * iWater.value()) / userPAmount.value();
                        log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                    }
                }
            }
            else if ((!e.isInput) && (userF.getOFlow1() != 0)) { // oWater 분별
                if (f.name.equals("Water/kg")) {
                    Converted oWater = convert.convertUnits(userF.getOFlow1(), userF.getOFlow1Unit());
                    log.info("isInput={}, e.amount of {} = {}", e.isInput, f.name, e.amount);
                    log.info("{} + ({} * {})/{}", e.amount, copyKoreaP.quantitativeReference.amount, oWater.value(), userPAmount.value());
                    e.amount = e.amount + (copyKoreaP.quantitativeReference.amount * oWater.value()) / userPAmount.value();
                    log.info("Adjusted Amount for {} = {}\n", e.flow.name, e.amount);
                }
            }
        }
        Process adjusted = addElec(userP, copyKoreaP, userPAmount);
        return adjusted;
    }

    Process addElec(SelectedProcess userP, Process copyKoreaP, Converted userPAmount) {
        UserFlows userF = userP.getFlows();
        ConvertUnits convert = new ConvertUnits();

        // 국가DB에서 전기 DB(=전기 flow) 가져오기
        Process elec = new ProcessDao(db).getForRefId("0bbf3ad4-480a-4b0b-bb86-5958945503a8");
        log.info("electric db = {}", elec);

        Exchange exElec = elec.quantitativeReference;
        Converted iElec = convert.convertUnits(userF.getIFlow1(), userF.getIFlow1Unit());
        exElec.amount = 1;

        log.info("before calc exElec amount = {} {}{}", exElec.flow.name, exElec.amount, exElec.unit.name);
        exElec.amount = (copyKoreaP.quantitativeReference.amount * iElec.value()) / userPAmount.value();
        log.info("{} = ({} * {})/{}", exElec.amount, copyKoreaP.quantitativeReference.amount, iElec.value(), userPAmount.value());
        exElec.unit = new UnitDao(db).getForName("kWh").get(0); // -> 무조건 kWh.
        exElec.isInput = true;
        exElec.defaultProviderId = elec.id;
        log.info("Add Elec = {}\n", exElec);

        copyKoreaP.exchanges.add(exElec);

        return copyKoreaP;
    }

    boolean getType(SelectedProcess userP, String type) {
        return userP.getType().name().equals(type);
    }

    ProductSystem createProductSystem(Process processes) {
        log.info("createProductSystem method start");
//        Process processes = new ProcessDao(db).getForName(productName).get(0);
        log.info("exchanges of processes in createProductSystem = {}, {}", processes.exchanges.size(), processes.exchanges);
        log.info("quantitativeReference = {}\n", processes.quantitativeReference);

        // create and auto-complete the product system
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var system = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(processes);

        log.info("ProductSystem info = {}", system);
        log.info("processes of system = {}, refP = {}, processLinks = {}, referenceExchange = {}",
                system.processes.size(), system.referenceProcess, system.processLinks, system.referenceExchange);
        log.info("exchanges.size = {}, processType = {}", system.referenceProcess.exchanges.size(), system.referenceProcess.processType);
        for (Exchange e : system.referenceProcess.exchanges) {
            log.info("exchanges of system = {}, {} {}", e, e.defaultProviderId, e.flow.flowType);
        }
        log.info("");

        // save the product system
        return new ProductSystemDao(db).insert(system);
//        return system;
    }

    void printLinkingStatus(ProductSystem system) {
        log.info("Linking status of ProductSystem:");
        log.info("system.processes.size = {}", system.processes.size());
/*
        for (Long pId : system.processes) {
            Process process = new ProcessDao(db).getForId(pId);
*/
        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        try (PrintWriter writer = new PrintWriter(prePath+"linkingTest.txt")) {
            var processes = new ProcessDao(db).getAll();
            writer.println("\n\nsaved processes imported db: ");
            writer.println("총 개수: " + processes.size());
            for (Process p : processes) {
                writer.println(p);
                writer.println(p.quantitativeReference+"\n\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Process process = system.referenceProcess;
        if (process != null) {

            log.info("Process: {}", process.name);
            log.info("process.exchanges.size = {}, quantitativeReference={}", process.exchanges.size(), process.quantitativeReference);
            for (Exchange exchange : process.exchanges) {
                Process provider = new ProcessDao(db).getForId(exchange.defaultProviderId);
                log.info("  Exchange: {} ({}) -> Provider: {}", exchange.flow.name,
                        exchange.flow.flowType, provider != null ? provider.name : "null");

            }
        } else {
            log.warn("Process is not found in the database.");
        }
        log.info("");
    }

    public LcaResult calculate(String methodName, ProductSystem system, Process processes, boolean removeFlag) {
        ImpactMethod method = new ImpactMethodDao(db).getForName(methodName).get(0);
        log.info("method = {}", method);
        CalculationSetup setup = CalculationSetup.of(system).withImpactMethod(method);
        SystemCalculator calc = new SystemCalculator(db);

        log.info("processes in calc = {}, {}, {}",
                processes.quantitativeReference,
                processes.processType,
                processes.exchanges.get(2).defaultProviderId);
        LcaResult result = calc.calculate(setup);
        ImpactDescriptor impact = result.impactIndex().at(8);
        log.info("total impact value: {}", result.getTotalImpactValueOf(impact)); // 총 카테고리 결과값
        log.info("total impact value: {}", result.getTotalImpacts()); // 총 카테고리 결과값
        log.info("total direct impact value: {}", result.getDirectImpactValuesOf(impact)); // 총 카테고리 결과값


        if (removeFlag) {
            log.info("remove to product system");
            new ProductSystemDao(db).deleteAll();
        }
        return result;
    }

    CategoryResultTable categoryResult(LcaResult r) {
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

    /**
     * impact -> impact factor 추출 -> techFlow(provider=공정별) -> enviFlow * factor(flow 별)
     */
    List<CategoryResultTable> categoryAllResult(LcaResult r) {
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

                    List<EnviFlowValue> factors = r.getImpactFactorsOf(impact); // Characterization Factor list 추출
                    if (factors.isEmpty())
                        log.warn("factor is empty.");
                    for (EnviFlowValue factor : factors) {
                        if (factor.value() > 0)
                            log.info("factor: {} = {}", factor.flow().name, factor.value());
                    }
                    log.info("");
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
    List<ProcessResultTable> processResult(LcaResult r, ImpactDescriptor impact, List<TechFlowValue> processResults) {
        log.info("Process별 결과값 추출");
        List<ProcessResultTable> processResultTables = new ArrayList<>();
        ProcessResultTable pTable;

        for (TechFlowValue processResult : processResults) {
            if (!Objects.equals(processResult.flow().name, productName)) {
                log.info("");
                log.info("{} = {}, {} = {}, value = {}",
                        processResult.provider().type, processResult.provider().name,
                        processResult.flow().flowType, processResult.flow().name, String.format("%.4f", processResult.value()));

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
    List<FlowResultTable> flowResult(LcaResult r, ImpactDescriptor impact, TechFlowValue processResult) {
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

    void getResultTables(List<CategoryResultTable> cgResultTables) {
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

    void saveResultTables(List<CategoryResultTable> cgResultTables) {

        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        try (PrintWriter writer = new PrintWriter(prePath+"resultTables.txt")) {
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

    @AfterEach
    void closeDb() throws IOException {
        runDatabase.closeDb();
        db = null;
    }

}
