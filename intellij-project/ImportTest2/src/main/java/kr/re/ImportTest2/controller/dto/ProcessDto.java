package kr.re.ImportTest2.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.UserFlows;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class ProcessDto {

    // process
    private Long id;

    private Long userId;

    @NotEmpty(message = "공정 이름을 작성해 주세요.")
    private String processName;

    private String selectedLciDb;
    private String mappedProcessId;

    @NotNull(message = "물질 및 부품 무게를 작성해 주세요.")
    private Double processAmount = 0.0;

    private String processAmountUnit;

    private ProcessType type;

    private UserFlows flows;

    /**
     *  flows
     *  -> "수송"의 경우: iFlow1, iFlow2를 이동거리, 차량 무게로 사용한다.
     */
    private Double iFlow1 = 0.0;
    private String iFlow1Unit;
    private Double iFlow2 = 0.0;
    private String iFlow2Unit;
    private Double oFlow1 = 0.0;
    private String oFlow1Unit = null;


    private UserFlows toFlowsEntity() {
        double oFlow1 = this.oFlow1 != null ? this.oFlow1 : 0.0;

        UserFlows f = UserFlows.builder()
                .iFlow1(iFlow1)
                .iFlow1Unit(iFlow1Unit)
                .iFlow2(iFlow2)
                .iFlow2Unit(iFlow2Unit)
                .oFlow1(oFlow1)
                .oFlow1Unit(oFlow1Unit)
                .build();
        return f;
    }
    public SelectedProcess toProcessEntity(User user, ProcessType type) {
        SelectedProcess sp = SelectedProcess.builder()
                .id(id)
                .user(user)
                .processName(processName)
                .mappedProcessId(mappedProcessId)
                .processAmount(processAmount)
                .processAmountUnit(processAmountUnit)
                .flows(toFlowsEntity()) // Embedded 니까 ...
                .type(type)
                .build();
        return sp;
    }

    public SelectedProcess toProcessEntity(User user) {
        SelectedProcess sp = SelectedProcess.builder()
                .id(id)
                .user(user)
                .processName(processName)
                .mappedProcessId(mappedProcessId)
                .processAmount(processAmount)
                .processAmountUnit(processAmountUnit)
                .flows(toFlowsEntity()) // Embedded 니까 ...
                .type(type)
                .build();
        return sp;
    }

    @Builder
    public ProcessDto(Long id, Long userId, String processName, String mappedProcessId, Double processAmount, String processAmountUnit, ProcessType type, UserFlows flows, Double iFlow1, String iFlow1Unit, Double iFlow2, String iFlow2Unit, Double oFlow1, String oFlow1Unit) {
        this.id = id;
        this.userId = userId;
        this.processName = processName;
        this.mappedProcessId = mappedProcessId;
        this.processAmount = processAmount;
        this.processAmountUnit = processAmountUnit;
        this.type = type;
        this.flows = flows;

        this.iFlow1 = iFlow1;
        this.iFlow1Unit = iFlow1Unit;
        this.iFlow2 = iFlow2;
        this.iFlow2Unit = iFlow2Unit;
        this.oFlow1 = oFlow1;
        this.oFlow1Unit = oFlow1Unit;
    }
}
