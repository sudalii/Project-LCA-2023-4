package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.Calculation;
import kr.re.ImportTest2.component.result.SystemBuilder;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.repository.CalcResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProductSystemDao;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CalcResultService {

    private final CalcResultRepository resultRepository;
    private final UserService userService;
    private final SelectedProcessService spService;

    private final SystemBuilder systemBuilder;
    private final Calculation calculation;
    private LcaResult cml;
    private LcaResult aware;

    public UserDto findUser(Long userId) {
        User user = userService.findUserById(userId);

        UserDto userDto = UserDto.builder()
                .productName(user.getProductName())
                .targetAmount(user.getTargetAmount())
                .targetUnit(user.getTargetUnit())
                .build();

        return userDto;
    }

    public List<SelectedProcess> findProcesses(Long userId) {
         return spService.findProcess(userId);
    }

    /**
     * LCA Calculation
     */
    @Transactional
    public void calculate(Long userId) {
        User user = userService.findUserById(userId);
        List<SelectedProcess> processes = spService.findProcess(userId);

        ProductSystem system = systemBuilder.run(user, processes);

        cml = calculation.calculate("CML-IA baseline", system, false);
        aware = calculation.calculate("AWARE", system, true);

        // result 값이 더이상 필요하지 않을 때 사용
        // r.dispose();
//        db = null;
    }

    @Transactional
    public List<CategoryResultTable> result() {
        List<CategoryResultTable> resultTables = calculation.categoryAllResult(cml);
        CategoryResultTable awareTable = calculation.categoryResult(aware);
        resultTables.add(awareTable);
        List<CategoryResultTable> sortedResultTables = CategoryResultTable.sortByImpactResultDescending(resultTables);

        calculation.saveResultTables(sortedResultTables);
        return sortedResultTables;
    }

    @Transactional
    public Long saveResult(CalcResultDto resultDto, Long userId) {
        CalcResultDto.builder().userId(userId).build();
        User user = userService.findUserById(userId);

        Long id = resultRepository.save(resultDto.toEntity(user)).getId();
        log.info("save 후 userId 저장(resultDto.getUserId()) 현황={}", resultDto.getUserId());
        log.info("save 후 user 저장 현황={}", user);
        return id;
    }
}

