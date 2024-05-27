package kr.re.ImportTest2.component.result;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.util.ConvertUnits;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import org.openlca.core.database.*;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
        Process processes = createPs.createProcesses(user);

        // 3. 제품 정보를 가지고 만들어진 processes 안에 각 선택된 국가DB와 계산하여 process 추가
        int n = 1;
        for (SelectedProcess p : ps) {
            log.info("({}).", n);
            createPs.addProcess(processes, p);
            n++;
        }
        Process updated = new ProcessDao(db).update(processes);

        // 4. 만들어진 Processes를 가지고 Product System 생성
        ProductSystem system = createProductSystem(updated);
        printLinkingStatus(system); // 추가: 링크 상태 출력
        return system;
    }

    private ProductSystem createProductSystem(Process processes) {
        log.info("createProductSystem method start");
        // create and auto-complete the product system
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var system = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(processes);

        log.info("processes of system = {}, refP = {}, processLinks = {}, referenceExchange = {}",
                system.processes.size(), system.referenceProcess, system.processLinks, system.referenceExchange);
        log.info("exchanges.size = {}, processType = {}", system.referenceProcess.exchanges.size(), system.referenceProcess.processType);
        for (Exchange e : system.referenceProcess.exchanges) {
            log.info("exchanges of system = {}, {} {}", e, e.defaultProviderId, e.flow.flowType);
        }
        log.info("");
        ProductSystem inserted = new ProductSystemDao(db).insert(system);
        name = inserted.name;

        return inserted;
    }

    void printLinkingStatus(ProductSystem system) {
        log.info("Linking status of ProductSystem:");
        log.info("system.processes.size = {}", system.processes.size());

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
