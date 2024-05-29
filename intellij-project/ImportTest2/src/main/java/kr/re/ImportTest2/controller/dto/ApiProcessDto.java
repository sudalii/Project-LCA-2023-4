package kr.re.ImportTest2.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.UserFlows;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class ApiProcessDto {

    private Long id;
    private Long userId;

    @NotEmpty(message = "공정 이름을 작성해 주세요.")
    private String processName;
    private String mappedProcessId;

    @NotNull(message = "물질 및 부품 무게를 작성해 주세요.")
    private Double processAmount = 0.0;
    private String processAmountUnit;

    private ProcessType type;

    private UserFlows flows;
    private Double iFlow1 = 0.0;
    private String iFlow1Unit;
    private Double iFlow2 = 0.0;
    private String iFlow2Unit;
    private Double oFlow1 = 0.0;
    private String oFlow1Unit = null;
    @Builder
    @JsonCreator
    public ApiProcessDto(
            @JsonProperty("id") Long id,
            @JsonProperty("userId") Long userId,
            @JsonProperty("processName") String processName,
            @JsonProperty("mappedProcessId") String mappedProcessId,
            @JsonProperty("processAmount") Double processAmount,
            @JsonProperty("processAmountUnit") String processAmountUnit,
            @JsonProperty("type") ProcessType type,
            @JsonProperty("iFlow1") Double iFlow1,
            @JsonProperty("iFlow1Unit") String iFlow1Unit,
            @JsonProperty("iFlow2") Double iFlow2,
            @JsonProperty("iFlow2Unit") String iFlow2Unit,
            @JsonProperty("oFlow1") Double oFlow1,
            @JsonProperty("oFlow1Unit") String oFlow1Unit) {

        this.id = id;
        this.userId = userId;
        this.processName = processName;
        this.mappedProcessId = mappedProcessId;

        if (type.equals(ProcessType.TRANSPORTATION)) {
            processAmount = iFlow1 * iFlow2;
            processAmountUnit = iFlow2Unit + "*" + iFlow1Unit;
        }
        this.processAmount = processAmount;
        this.processAmountUnit = processAmountUnit;
        this.type = type;

        this.iFlow1 = iFlow1;
        this.iFlow1Unit = iFlow1Unit;
        this.iFlow2 = iFlow2;
        this.iFlow2Unit = iFlow2Unit;
        this.oFlow1 = oFlow1;
        this.oFlow1Unit = oFlow1Unit;
    }

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
    public SelectedProcess toProcessEntity(User user) {
        return SelectedProcess.builder()
                .id(id)
                .user(user)
                .processName(processName)
                .mappedProcessId(mappedProcessId)
                .processAmount(processAmount)
                .processAmountUnit(processAmountUnit)
                .flows(toFlowsEntity())
                .type(type)
                .build();
    }
}
