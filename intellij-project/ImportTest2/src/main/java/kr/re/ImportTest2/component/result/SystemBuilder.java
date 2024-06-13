package kr.re.ImportTest2.component.result;

import gnu.trove.impl.Constants;
import gnu.trove.set.hash.TLongHashSet;
import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.util.ConvertUnits;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import org.openlca.core.database.*;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.index.LongPair;
import org.openlca.core.matrix.index.TechFlow;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.util.Processes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SystemBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final static RunDatabase runDatabase = new RunDatabase();
    protected static IDatabase db = null;
    public static String name;

    public ProductSystem run(User user, List<SelectedProcess> ps) {
         // 1. run Derby DB
        db = runDatabase.runDb();
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return null;
        }
        ConvertUnits.db = db;   // 계산 중 유닛 변환을 위한 Class에 db 적재

        // 2. Product를 만들기 위해 여러 process들을 하나의 process로 담아서 만들기 위한 processes 생성
        // -> 여기서 만들어진 Processes가 그대로 Product System이 될 것임
        CreateProcesses createPs = new CreateProcesses();
        Process newP = createPs.createProcesses(user);
        Process processes = newP.copy();

        // 3. 제품 정보를 가지고 만들어진 processes 안에 각 선택된 국가DB와 계산하여 process 추가
        int n = 1;
        for (SelectedProcess p : ps) {
            log.info("({}).", n);
            createPs.addProcess(processes, p);
            n++;
        }
        Process updated = new ProcessDao(db).update(processes);
//        log.info("updated process: {}", updated);

        // 4. 만들어진 Processes를 가지고 Product System 생성
        ProductSystem system = createProductSystem(updated, newP);
//        printLinkingStatus(system); // 추가: 링크 상태 출력
        return system;
    }

    public Process run(User user, List<SelectedProcess> ps, boolean flag) {
        // 1. run Derby DB
        db = runDatabase.runDb();
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return null;
        }
        ConvertUnits.db = db;   // 계산 중 유닛 변환을 위한 Class에 db 적재

        // 2. Product를 만들기 위해 여러 process들을 하나의 process로 담아서 만들기 위한 processes 생성
        // -> 여기서 만들어진 Processes가 그대로 Product System이 될 것임
        CreateProcesses createPs = new CreateProcesses();
        Process processes = createPs.createProcesses(user);

        // 3. 제품 정보를 가지고 만들어진 processes 안에 각 선택된 국가DB와 계산하여 process 추가
        int n = 1;
        for (SelectedProcess p : ps) {
            log.info("({}).", n);
            createPs.addProcess(processes, p);
            n++;
        }
        // 4. 만들어진 Processes를 가지고 Direct로 계산
        Process updated = new ProcessDao(db).update(processes);
        if (updated != null) {
            log.info("Process: {}", updated.name);
            log.info("process.exchanges.size = {}, quantitativeReference={}", updated.exchanges.size(), updated.quantitativeReference);
            for (Exchange exchange : updated.exchanges) {
                Process provider = new ProcessDao(db).getForId(exchange.defaultProviderId);
                log.info("  Exchange: {} ({}) -> Provider: {}", exchange.flow.name,
                        exchange.flow.flowType, provider != null ? provider.name : "null");
            }
        }
        log.info("isMultiFunctional: {}", Processes.isMultiFunctional(updated));
        log.info("getProviderFlows:");
        for (Exchange e : Processes.getProviderFlows(updated)) {
            log.info("{}", e);
        }
        return updated;
    }

    private ProductSystem buildProductSystem(Process processes) {
        log.info("createProductSystem method start");
        log.info("processes:");
        log.info("{}", processes);
        for (Exchange e : processes.exchanges) {
            log.info("e: {} {}", e.flow.name, e.amount);
            Process provider = new ProcessDao(db).getForId(e.defaultProviderId);
            log.info("refP: {} refId:{} id:{}", provider != null ? provider.name : "null",
                                    provider != null ? provider.refId : "null",
                                    provider != null ? provider.id : "null");
        }
        // create and auto-complete the product system
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var system = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(processes);

        log.info("processes of system = {}, refP = {}, processLinks = {}, referenceExchange = {}",
                system.processes.size(), system.referenceProcess, system.processLinks, system.referenceExchange);
        for(Process p : new ProcessDao(db).getForIds(system.processes)) {
            log.info("processes of system: {}, refId:{}, id:{}", p.name, p.refId, p.id);
        }

        log.info("exchanges.size = {}, processType = {}", system.referenceProcess.exchanges.size(), system.referenceProcess.processType);
        /*for (Exchange e : system.referenceProcess.exchanges) {
            log.info("exchanges of system = {}, {} {}", e, e.defaultProviderId, e.flow.flowType);
        }*/
        log.info("");
        ProductSystem inserted = new ProductSystemDao(db).insert(system);
        name = inserted.name;

        return inserted;
    }

    private ProductSystem createProductSystem(Process processes, Process newP) {
        log.info("createProductSystem method start");
        log.info("processes:");
        log.info("{}", processes);
        int n = 0;
//        ProductSystem pd = new ProductSystem();
        List<ProductSystem> exist = new ProductSystemDao(db).getForName(processes.name);
        if (!exist.isEmpty()) {
            for (ProductSystem pds : exist) {
                log.info("delete product of {}", pds.name);
                new ProductSystemDao(db).delete(pds);
            }
        }
//        ProductSystem system = new ProductSystem();
//        system.referenceProcess = processes;
        ProductSystem system = ProductSystem.of(processes);
        TechIndex index = new TechIndex();
//        Map<Exchange, Process> providers = new HashMap<>();
        for (Exchange e : processes.exchanges) {
            log.info("({}).", n);
            log.info("e: {} {}", e.flow.name, e.amount);
            Process provider = new ProcessDao(db).getForId(e.defaultProviderId);
            e.defaultProviderId = 0;
            db.update(processes);
            if (provider == null) {
//                e.defaultProviderId = 0;
                db.update(processes);
            }else {
                log.info("refP: {} refId:{} id:{}", provider.name, provider.refId, provider.id);
/*                pToPd = db.insert(ProductSystem.of(provider));
                pToPd.targetAmount = e.amount;
                pToPd.targetUnit = e.unit;
                ProductSystem link = pToPd.link(TechFlow.of(pd), provider);
                TechIndex ti = TechIndex.of(db, link);*/

/*                log.info("e.id: {}, TechFlow.of(provider): {}", e.id, TechFlow.of(provider));
                LongPair pairEx = LongPair.of(provider.id, e.id);
                index.putLink(pairEx, TechFlow.of(provider));
                addLinksAndProcesses(system, index);*/
                TechFlow tf = TechFlow.of(provider);
/*                log.info("TechFlow.of(provider): ");
                log.info("provider: {} - {}", tf.providerId(), tf.provider());
                log.info("flow: {} - {}", tf.flowId(), tf.flow());*/
                system.link(tf, processes);

            }
            n++;
        }
        db.insert(system);

        // create and auto-complete the product system
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        new ProductSystemBuilder(MatrixCache.createLazy(db), config).autoComplete(system);
/*        var updated = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(system);*/
        ProductSystem updated = ProductSystemBuilder.update(db, system);

        log.info("processes of system = {}, refP = {}, processLinks num = {}, referenceExchange = {}",
                updated.processes.size(), updated.referenceProcess, updated.processLinks.size(), updated.referenceExchange);
        for (ProcessLink link : updated.processLinks) {
            Process process = new ProcessDao(db).getForId(link.processId);
            Process provider = new ProcessDao(db).getForId(link.providerId);
            log.info("processLinks: process {}, {} -> provider {}, {}", link.processId, process.name,
                    link.providerId, provider.name);

        }
        for (Process p : new ProcessDao(db).getForIds(updated.processes)) {
            log.info("processes of system: {}, refId:{}, id:{}, exchanges.size:{}", p.name, p.refId, p.id, p.exchanges.size());
            if (p.name.equals("시나리오 테스트2")) {
                for (Exchange e : p.exchanges) {
                    log.info("e: {} - {}{}, {}", e.flow.name, e.amount, e.unit.name, e.isInput);
                }
            }
        }
//        log.info("exchanges.size = {}, processType = {}", system.referenceProcess.exchanges.size(), system.referenceProcess.processType);
        /*for (Exchange e : system.referenceProcess.exchanges) {
            log.info("exchanges of system = {}, {} {}", e, e.defaultProviderId, e.flow.flowType);
        }*/
        log.info("");
//        ProductSystem inserted = new ProductSystemDao(db).insert(system);
        name = updated.name;

        return updated;
    }

    private void addLinksAndProcesses(ProductSystem system, TechIndex index) {
        var linkIds = new TLongHashSet(Constants.DEFAULT_CAPACITY,
                Constants.DEFAULT_LOAD_FACTOR, -1);
        for (LongPair exchange : index.getLinkedExchanges()) {
            var provider = index.getLinkedProvider(exchange);
            if (provider == null)
                continue;
            system.processes.add(provider.providerId());
            system.processes.add(exchange.first());
            long exchangeId = exchange.second();
            if (linkIds.add(exchangeId)) {
                var link = new ProcessLink();
                link.exchangeId = exchangeId;
                link.flowId = provider.flowId();
                link.processId = exchange.first();
                link.providerId = provider.providerId();
                link.providerType = ProcessLink.ProviderType.PROCESS;
                system.processLinks.add(link);
            }
        }
    }

    void printLinkingStatus(ProductSystem system) {
        log.info("Linking status of ProductSystem:");
        log.info("system.processes.size = {}", system.processes.size());
        List<Process> ps = new ProcessDao(db).getForIds(system.processes);
        for (Process p : ps) {
            log.info("{}", p);
        }
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

    public static void closeDb() throws IOException {
        runDatabase.closeDb();
        db = null;
    }
}
