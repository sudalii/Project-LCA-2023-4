package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.result.Calculation;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.repository.CalcResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openlca.core.database.IDatabase;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CalcResultService {

    private final UserService userService;
    private final CalcResultRepository resultRepository;
    private final SelectedProcessService spService;
    private final Calculation calculation;
    private LcaResult cml;
    private LcaResult aware;

/*
    public Long findAll(Long userId) {
        resultRepository.findAllById();
    }
*/

    public User findUser(Long userId) {
        return userService.findUserById(userId);
    }

    @Transactional
    public void calculate() {
        IDatabase db = spService.db;
        if (db == null) {
            log.error("db is null, it does not come over to Calculation class");
            return;
        }
        calculation.system = spService.system;
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

