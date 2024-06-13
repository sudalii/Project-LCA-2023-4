package kr.re.ImportTest2.result;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ImpactMethodDao;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.database.ProductSystemDao;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.results.LcaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
public class ProductSystemTest {

    private final RunDatabase runDatabase;
    protected static IDatabase db = null;
    private final String productName = "ProductName01";

    @Autowired
    public ProductSystemTest(RunDatabase runDatabase) {
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

//    @Test
    void importProductSystem() {
//        ProductSystem system = new ProductSystemDao(db).getForName("PlasticProduct01").get(0);
        Process p = new ProcessDao(db).getForName("PlasticProduct01-0530").get(0);
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var system = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(p);
        log.info("processes of system = {}, refP = {}, processLinks num = {}, referenceExchange = {}",
                system.processes.size(), system.referenceProcess, system.processLinks.size(), system.referenceExchange);
        for (ProcessLink link : system.processLinks) {
            Process process = new ProcessDao(db).getForId(link.processId);
            Process provider = new ProcessDao(db).getForId(link.providerId);
            log.info("processLinks: process {}, {} -> provider {}, {}", link.processId, process.name,
                    link.providerId, provider != null ? provider.name : "null");
        }
        for (Process ps : new ProcessDao(db).getForIds(system.processes)) {
            log.info("processes of system: {}, refId:{}, id:{}, exchanges.size:{}", ps.name, ps.refId, ps.id, ps.exchanges.size());
//            if (ps.name.equals("PlasticProduct01-0530")) {
                for (Exchange e : ps.exchanges) {
                    Process provider = new ProcessDao(db).getForId(e.defaultProviderId);
//                    log.info("e: {} - {}{}, {}", e.flow.name, e.amount, e.unit.name, e.isInput);
                    log.info("  Exchange: {} ({}) -> Provider: {}", e.flow.name,
                            e.flow.flowType, provider != null ? provider.name : "null");
                }
//            }
        }
        var method = new ImpactMethodDao(db).getForName("CML-IA baseline").get(0);
        log.info("method find: {}", method);
        CalculationSetup setup = CalculationSetup.of(system).withImpactMethod(method);
        SystemCalculator calc = new SystemCalculator(db);
        var r = calc.calculate(setup);
        new ProductSystemDao(db).deleteAll();

        var f = r.enviIndex().at(0);
        System.out.println(f.flow().name + "  -> " + r.getTotalFlowValueOf(f));
        var impact =  r.impactIndex().at(0);
        System.out.println(impact.name + "  -> " + r.getTotalImpactValueOf(impact));
        r.dispose();
    }

    @AfterEach
    void closeDb() throws IOException {
        runDatabase.closeDb();
        db = null;
    }
}
