package kr.re.ImportTest2.result;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
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
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.model.descriptors.ImpactDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
public class ProductSystemInMemoryCalculationExample {

    private final RunDatabase runDatabase;

    protected static IDatabase db = null;
    private final String productName = "ProductName01";

    @Autowired
    public ProductSystemInMemoryCalculationExample(RunDatabase runDatabase) {
        this.runDatabase = runDatabase;
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
    void compareWithCustomLci() {

        List<Exchange> ps = new ArrayList<>();
        List<Exchange> koreaPs = new ArrayList<>();

        Process p = new ProcessDao(db).getForName("P2-1").get(0);
        Process koreaP = new ProcessDao(db).getForName("PVC(Poly Vinyl Chloride)").get(0);

        // 검증 코드
        log.info("p = {}, type = {}", p, p.processType);
        for (Exchange e: p.exchanges) {
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
        log.info("");
        log.info("koreaP = {}, type = {}", p, p.processType);
        for (Exchange e: koreaP.exchanges) {
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
/*        log.info("p = {}, type = {}", p, p.processType);
        for (Exchange e : p.exchanges) {
            log.info("exchange: {}", e);
        }*/

        // load the database and matrix cache
        MatrixCache mcache = MatrixCache.createLazy(db);
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var builder = new ProductSystemBuilder(mcache, config);
        var system = builder.build(p);
        var method = new ImpactMethodDao(db).getForName("CML-IA baseline").get(0);

        log.info("method = {}", method);

        // create the calculation setup
        var setup = CalculationSetup.of(system)
                .withImpactMethod(method);

        // load the native library and calculate the result
        // TODO: load Julia libraries first here
        SystemCalculator calc = new SystemCalculator(db);
        var r = calc.calculate(setup);

        // print the LCIA results
        for (ImpactDescriptor impact : r.impactIndex()) {
            System.out.println(impact.name + "\t"
                    + r.getTotalImpactValueOf(impact) + "\t"
                    + impact.referenceUnit);
        }

        // clean up
        r.dispose();
    }
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

    @Test
    void multiKoreaProcess() {

        List<Exchange> ps = new ArrayList<>();
        List<Exchange> koreaPs = new ArrayList<>();

//        Process processes = new ProcessDao(db).getForName("ProductName01").get(0);
        Process processes = createProcess(createProductBasedFlow());
        Process koreaP1 = new ProcessDao(db).getForName("HPP(Homo Poly Propylene)").get(0);
        Process koreaP2 = new ProcessDao(db).getForName("mixed_plastic_landfill").get(0);
        Process koreaP3 = new ProcessDao(db).getForName("Recycling_Pellet_Waste_Plastic").get(0);
        Process incineration = new ProcessDao(db).getForName("Mixed plastics incineration").get(0);
        log.info("incineration: {}, {}", incineration.id, incineration.refId);
/*        log.info("before processes: {}", processes);
        for (Exchange e : processes.exchanges) {
            log.info("e = {}", e);
        }*/

//        Process processes = new Process();
//        Process processes = new ProcessDao(db).getForName("ProductName01").get(0);
//        processes.name = "ProductName01";
//        processes.quantitativeReference = Exchange.of(new FlowDao(db).getForName("ProductName01").get(0));        processes.quantitativeReference.amount = 50;
//        processes.quantitativeReference.unit = new UnitDao(db).getForName("t").get(0);

        Exchange ref = koreaP1.quantitativeReference;
        ref.isInput = true;
        ref.defaultProviderId = koreaP1.id;

        Exchange ref2 = koreaP2.quantitativeReference;
        ref2.isInput = true;
        ref2.defaultProviderId = koreaP2.id;

        Exchange ref3 = koreaP3.quantitativeReference;
        ref3.isInput = false;
        ref3.defaultProviderId = koreaP3.id;


        processes.exchanges.add(ref);
        processes.exchanges.add(ref2);
        processes.exchanges.add(ref3);
        for (Exchange e : processes.exchanges) {
            log.info("e name = {}, id = {}, internalId = {}, providerId = {}", e.flow.name, e.id, e.internalId, e.defaultProviderId);
        }
        long pId = processes.id;
        new ProcessDao(db).update(processes);
//        Process inserted = new ProcessDao(db).insert(processes);

        Process inserted = new ProcessDao(db).getForId(pId);
        log.info("processes = {}, type = {}", inserted, inserted.processType);
        for (Exchange e : inserted.exchanges) {
            log.info("exchange: {}", e);
        }

        // load the database and matrix cache
        MatrixCache mcache = MatrixCache.createLazy(db);
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);

        var builder = new ProductSystemBuilder(mcache, config);
        var system = builder.build(processes);
        log.info("system.processes.size = {}", system.processes.size());
        List<Process> systemP = new ProcessDao(db).getForIds(system.processes);
        for (Process p : systemP) {
            log.info("{}", p);
        }
        var method = new ImpactMethodDao(db).getForName("CML-IA baseline").get(0);

        log.info("method = {}", method);

        // create the calculation setup
        var setup = CalculationSetup.of(system)
                .withImpactMethod(method);

        // load the native library and calculate the result
        // TODO: load Julia libraries first here
        SystemCalculator calc = new SystemCalculator(db);
        var r = calc.calculate(setup);

        // print the LCIA results
        for (ImpactDescriptor impact : r.impactIndex()) {
            System.out.println(impact.name + "\t"
                    + r.getTotalImpactValueOf(impact) + "\t"
                    + impact.referenceUnit);
        }

        // clean up
        r.dispose();
        new ProcessDao(db).delete(inserted);
    }

    @Test
    void directCalcToProcess() {

        List<Exchange> ps = new ArrayList<>();
        List<Exchange> koreaPs = new ArrayList<>();

//        Process processes = new ProcessDao(db).getForName("ProductName01").get(0);
        Process processes = createProcess(createProductBasedFlow());
        Process koreaP1 = new ProcessDao(db).getForName("HPP(Homo Poly Propylene)").get(0);
        Process koreaP2 = new ProcessDao(db).getForName("mixed_plastic_landfill").get(0);
        Process koreaP3 = new ProcessDao(db).getForName("Recycling_Pellet_Waste_Plastic").get(0);

        for (Exchange e : processes.exchanges) {
            log.info("e name = {}, id = {}, internalId = {}, providerId = {}", e.flow.name, e.id, e.internalId, e.defaultProviderId);
        }
        long pId = processes.id;
        new ProcessDao(db).update(processes);
//        Process inserted = new ProcessDao(db).insert(processes);

        Process inserted = new ProcessDao(db).getForId(pId);
        log.info("processes = {}, type = {}", inserted, inserted.processType);
        for (Exchange e : inserted.exchanges) {
            log.info("exchange: {}", e);
        }

        // load the database and matrix cache
        MatrixCache mcache = MatrixCache.createLazy(db);
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);

        var builder = new ProductSystemBuilder(mcache, config);
        var system = builder.build(processes);
        log.info("system.processes.size = {}", system.processes.size());
        List<Process> systemP = new ProcessDao(db).getForIds(system.processes);
        for (Process p : systemP) {
            log.info("{}", p);
        }

//        system.processes.add()

        var method = new ImpactMethodDao(db).getForName("CML-IA baseline").get(0);

        log.info("method = {}", method);

        // create the calculation setup
        var setup = CalculationSetup.of(system)
                .withImpactMethod(method);

        // load the native library and calculate the result
        // TODO: load Julia libraries first here
        SystemCalculator calc = new SystemCalculator(db);
        var r = calc.calculate(setup);

        // print the LCIA results
        for (ImpactDescriptor impact : r.impactIndex()) {
            System.out.println(impact.name + "\t"
                    + r.getTotalImpactValueOf(impact) + "\t"
                    + impact.referenceUnit);
        }

        // clean up
        r.dispose();
        new ProcessDao(db).delete(inserted);
    }

    void saveFlows(Process p, Process koreaP) {
        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        try (PrintWriter writer = new PrintWriter(prePath+"p.txt")) {
            writer.println("userP" + p + "type: " + p.processType);
            for (Exchange e : p.exchanges) {
                writer.println("exchange: " + e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (PrintWriter writer = new PrintWriter(prePath+"koreaP.txt")) {
            writer.println("koreaP" + koreaP + "type: " + koreaP.processType);
            for (Exchange e : koreaP.exchanges) {
                writer.println("exchange: " + e);
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
