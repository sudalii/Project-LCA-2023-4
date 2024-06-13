package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.Calculation;
import kr.re.ImportTest2.component.result.SystemBuilder;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.CalcResult;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.repository.CalcResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.database.ProductSystemDao;
import org.openlca.core.model.Process;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.results.LcaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private Long userIdForUpdate;
//    public static List<FlowResultTable> flowTables;

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

    public List<CalcResult> findResults(Long userId) {
        return resultRepository.findAllByUserId(userId);
    }

    public List<CalcResultDto> findResultDtos(Long userId) {
        List<CalcResult> allByUserId = resultRepository.findAllByUserId(userId);

        List<CalcResultDto> resultDtos = new ArrayList<>();
        for (CalcResult re : allByUserId) {
            CalcResultDto resultDto = CalcResultDto.builder()
                    .id(re.getId())
                    .userId(re.getUser().getId())
                    .categoryName(re.getCategoryName())
                    .results(re.getResults())
                    .flowTables(re.getFlowTables())
                    .build();
            resultDtos.add(resultDto);
        }
        return resultDtos;
    }

    /**
     * LCA Calculation
     */
    public void calculate(Long userId) {
        userIdForUpdate = userId;
        User user = userService.findUserById(userId);
        List<SelectedProcess> processes = spService.findProcess(userId);

        ProductSystem system = systemBuilder.run(user, processes);
//        Process updated = systemBuilder.run(user, processes, true);
        // IPCC 2021, CML-IA baseline
        cml = calculation.calculate("CML-IA baseline", system, null, false);
        aware = calculation.calculate("AWARE", system, null, true);
        // result 값이 더이상 필요하지 않을 때 사용
        // r.dispose();
//        db = null;
    }

    @Transactional
    public List<CategoryResultTable> resultForApi() {
        List<CategoryResultTable> resultTables = calculation.categoryAllResultForApi(cml);
        CategoryResultTable awareTable = calculation.categoryResultForApi(aware);
        resultTables.add(awareTable);
        List<CategoryResultTable> sortedResultTables = CategoryResultTable.sortByImpactResultDescending(resultTables);

        calculation.saveResultTables(sortedResultTables);
        return sortedResultTables;
    }

    public Pair<CategoryResultTable, List<FlowResultTable>> resultCml(String cgOriName) {
        return calculation.getResultOne(cml, cgOriName);
    }

    public Pair<CategoryResultTable, List<FlowResultTable>> resultAware(String cgOriName) {
        return calculation.getResultOne(aware, cgOriName);
    }

    @Transactional
    public void calcAndResult(Long userId) {
        calculate(userId);

    }

    @Transactional
    public void saveResults(CategoryResultTable cgTable, List<FlowResultTable> flowTables) {
        CalcResultDto resultDto = CalcResultDto.builder().build();

        User user = userService.findUserById(userIdForUpdate);
        Long id = resultRepository.save(resultDto.toEntity(user, cgTable, flowTables)).getId();
        log.info("save 후 userId 저장(resultDto.getUserId()) 현황={}", resultDto.getUserId());
        log.info("save 후 user 저장 현황={}", user);
//        return id;
    }

    @Transactional
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }
/*    @Transactional
    public Long saveResults(CalcResultDto resultDto, Long userId) {
        CalcResultDto.builder().userId(userId).build();
        User user = userService.findUserById(userId);

        Long id = resultRepository.save(resultDto.toEntity(user)).getId();
        log.info("save 후 userId 저장(resultDto.getUserId()) 현황={}", resultDto.getUserId());
        log.info("save 후 user 저장 현황={}", user);
        return id;
    }*/

}

