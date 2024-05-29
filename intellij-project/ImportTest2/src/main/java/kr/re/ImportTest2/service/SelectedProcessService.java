package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.derdyDb.RunDatabase;
import kr.re.ImportTest2.component.result.SystemBuilder;
import kr.re.ImportTest2.controller.dto.ApiProcessDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SelectedProcessService {

    private final SelectedProcessRepository spRepository;
    private final UserRepository userRepository;


    /**
     * build 패턴 사용 - lombok의 build 사용
     */
    @Transactional
    public void saveProcessForApi(ApiProcessDto processDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        log.info("ProcessDto to be saved:");
        log.info("IFlow1: {} {}", processDto.getIFlow1(), processDto.getIFlow1Unit());

/*        UserFlows f = pDto.getFlows();
        ProcessDto processDto = ProcessDto.builder()
                // process
                .userId(userId)
                .processName(pDto.getProcessName())
                .mappedProcessId(pDto.getMappedProcessId())
                .processAmount(pDto.getProcessAmount())
                .processAmountUnit(pDto.getProcessAmountUnit())
                .type(pDto.getType())
                // flows
                .iFlow1(f.getIFlow1())
                .iFlow1Unit(f.getIFlow1Unit())
                .iFlow2(f.getIFlow2())
                .iFlow2Unit(f.getIFlow2Unit())
                .oFlow1(f.getOFlow1())
                .oFlow1Unit(f.getOFlow1Unit())
                .build();*/
        spRepository.save(processDto.toProcessEntity(user));
    }

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

    public List<ProcessDto> findAllByType(Long userId, ProcessType type) {
        List<SelectedProcess> allByUserId = spRepository.findAllByUserId(userId);
        List<SelectedProcess> typeList = new ArrayList<>();

        if (!allByUserId.isEmpty()) {
            for(SelectedProcess findById : allByUserId) {
                if (findById.getType().equals(type)) {
                    typeList.add(findById);
                }
            }
        } else {
            log.info("allByUserId is empty.");
        }

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

    public List<SelectedProcess> findProcess(Long userId) {
        return spRepository.findAllByUserId(userId);
    }
}
