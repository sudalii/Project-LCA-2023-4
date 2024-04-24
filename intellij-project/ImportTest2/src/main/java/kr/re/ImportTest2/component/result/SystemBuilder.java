package kr.re.ImportTest2.component.result;

import lombok.RequiredArgsConstructor;
import org.openlca.core.database.*;
import org.openlca.core.matrix.ProductSystemBuilder;
import org.openlca.core.matrix.cache.MatrixCache;
import org.openlca.core.matrix.linking.LinkingConfig;
import org.openlca.core.matrix.linking.ProviderLinking;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class SystemBuilder {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    private ProductSystem system = new ProductSystem();
    private static IDatabase db = null;

    public final double TARGET_AMOUNT = 0.0;
    public final String TARGET_UNIT = null;
    public final String PRODUCT_NAME = null;

    /**
     * select된 process들을 가져와서 product system을 만든다.
     * - Process 가져오는 방법
     *  1. 화면에서 각 공정별로 Process select
     *  2. controller에서 넘겨받은 selected process id 리스트로? 가져오기
     *  3.
     *  그리고, controller 단에서 직전에 수행된 메서드에서 db를 return 해 받는다.
     *  그 db를 param 으로 넣으면 될 듯
     */
    public void addProcess(IDatabase buildDb, String processId, double amount) {
        db = buildDb;
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return;
        }
        // 사용자가 입력한 process name에 매칭된 process id으로
        // db(==국가DB)를 검색해서 가져오기
        Process koreaDb = new ProcessDao(db).getForId(Long.parseLong(processId));
        Process process = CustomizeAProcess(koreaDb);

        // baseFlow: Process provider를 담을 flow
        Flow baseFlow = new FlowDao(db).getForName(PRODUCT_NAME).get(0);
        if (baseFlow == null) {
            baseFlow = createFlow();
        }
//        baseFlow.flowType // 수정하기 !!!

        // 여러 process들을 flow로 받아서 product system으로 만들 process 가져오기
        Process processes = new ProcessDao(db).getForName(PRODUCT_NAME).get(0);
        if (processes == null) {
            processes = createProcess(baseFlow);
        }

        Exchange input = processes.input(baseFlow, amount);
        input.defaultProviderId = process.id;

        db = null;
    }

    /**
     * user input values 어떻게 가져올건지 고민.
     * 1. db에서 직접 꺼내오기
     * 2. param으로 받아오기
     */
    private Process CustomizeAProcess(Process p) {


        return p;
    }

    private Flow createFlow() {
        FlowProperty mass = new FlowPropertyDao(db).getForName("Mass").get(0);
        Flow f = Flow.product(PRODUCT_NAME, mass);
        new FlowDao(db).insert(f);

        return f;
    }

    private Process createProcess(Flow baseFlow) {
        Process p = Process.of(PRODUCT_NAME, baseFlow);
        new ProcessDao(db).insert(p);

        return p;
    }

    public ProductSystem createProductSystem(IDatabase buildDb) {
        db = buildDb;
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return null;
        }
        Process processes = new ProcessDao(db).getForName(PRODUCT_NAME).get(0);

        // create and auto-complete the product system
        var config = new LinkingConfig()
                .providerLinking(ProviderLinking.PREFER_DEFAULTS)
                .preferredType(ProcessType.UNIT_PROCESS);
        var system = new ProductSystemBuilder(MatrixCache.createLazy(db), config)
                .build(processes);

        system.targetAmount = TARGET_AMOUNT;
        system.targetUnit = Unit.of(TARGET_UNIT);

        // save the product system
        new ProductSystemDao(db).insert(system);
        db = null;

        return system;
    }
}
