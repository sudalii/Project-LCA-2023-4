package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.SystemBuilder;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.UserFlows;
import kr.re.ImportTest2.repository.SelectedProcessRepository;
import kr.re.ImportTest2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.ProductSystem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SelectedProcessService {

    private final SelectedProcessRepository spRepository;
    private final UserRepository userRepository;
    private final RunDatabase runDatabase;
    private final SystemBuilder systemBuilder;

    public IDatabase db = null;
    public ProductSystem system = null;

    /**
     * build 패턴 사용 - lombok의 build 사용
     */
    @Transactional
    public Long saveProcess(ProcessDto processDto, Long userId, String type) {
        log.info("type = {} -> {}", type, ProcessType.of(type));
        ProcessDto.builder().userId(userId).build();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Not found userId for saveProcess: " + processDto.getUserId()));

        Long id = spRepository.save(processDto.toProcessEntity(user, ProcessType.of(type))).getId();
        log.info("save 후 userId 저장(processDto.getUserId()) 현황={}", processDto.getUserId());
        log.info("save 후 user 저장 현황={}", user);
        return id;
    }

    @Transactional
    public ProcessDto updateForm(Long pId) {
        SelectedProcess p = spRepository.findById(pId).orElseThrow(
                () -> new RuntimeException("Not found pId for updateForm: " + pId));
        UserFlows f = p.getFlows();
        // 참고: ? 문법 사용법 --> value != null ? next(value.doubleValue()) : next();

        log.info("p.getId in updateForm = {}, pId = {}", p.getId(), pId);

        ProcessDto processDto = ProcessDto.builder()
                // process
                .id(p.getId())
                .userId(p.getUser().getId())
                .processName(p.getProcessName())
                .mappedProcessId(p.getMappedProcessId())
                .processAmount(p.getProcessAmount())
                .processAmountUnit(p.getProcessAmountUnit())
                .type(p.getType())
                // flows
                .iFlow1(f.getIFlow1())
                .iFlow1Unit(f.getIFlow1Unit())
                .iFlow2(f.getIFlow2())
                .iFlow2Unit(f.getIFlow2Unit())
                .oFlow1(f.getOFlow1())
                .oFlow1Unit(f.getOFlow1Unit())
                .build();

        return processDto;
    }

    // save update data
    @Transactional
    public void updateProcess(ProcessDto processDto) {
        User user = userRepository.findById(processDto.getUserId()).orElseThrow(
                () -> new RuntimeException("Not found pId for updateProcess: " + processDto.getUserId()));
        spRepository.save(processDto.toProcessEntity(user));
    }

    @Transactional
    public void deleteProcess(Long id) {
        spRepository.deleteById(id);
    }

    public List<ProcessDto> findAllByType(ProcessType type) {
        List<SelectedProcess> typeList = spRepository.findAllByType(type);
        List<ProcessDto> pDtoList = new ArrayList<>();

        for(SelectedProcess p : typeList) {
            ProcessDto processDto = ProcessDto.builder()
                    .id(p.getId())
                    .processName(p.getProcessName())
                    .build();
            pDtoList.add(processDto);
        }
        return pDtoList;
    }

    /**
     * p4, 수송에만 해당되는 메서드. JS로 처리할 수도 있음
     * Dto 저장 직전 해당 메서드 호출하면 될 듯
     *  - processAmount = 이동거리(iFlow1) * 차량무게(iFlow2)
     *  - processAmountUnit = iFlow1Unit*iFlow2Unit
     */
    public void calcP4ProcessAmount() {

    }

    public void runDb() {
        db = runDatabase.runDb();
    }

    public void addProcess(String processId, double amount) {
        systemBuilder.addProcess(db, processId, amount);
    }

    public void systemBuilder() {
        system = systemBuilder.createProductSystem(db);
    }

    public void closeDb() throws IOException {
        runDatabase.closeDb();
        db = null;
    }
}
