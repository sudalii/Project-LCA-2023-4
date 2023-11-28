package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.result.Calculation;
import lombok.RequiredArgsConstructor;
import org.openlca.core.database.IDatabase;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResultService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SelectedProcessService dbService;
    private final Calculation calculation;
    private LcaResult cml;
    private LcaResult aware;

    @Transactional
    public void calculate() {
        IDatabase db = dbService.db;
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return;
        }
        calculation.system = dbService.system;
        cml = calculation.calculate("CML-IA baseline");
        aware = calculation.calculate("AWARE");

        // result 값이 더이상 필요하지 않을 때 사용
        // r.dispose();
//        db = null;
    }

    @Transactional
    public void result() {

        calculation.categoryResult(cml);
        calculation.categoryResult(aware);
    }
}

