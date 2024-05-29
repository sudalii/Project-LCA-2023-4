package kr.re.ImportTest2.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.re.ImportTest2.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class ApiUserDto {

    private Long id;

    @NotEmpty(message = "제품 이름을 작성해 주세요.")
    private final String productName;

    @NotNull(message = "제품양을 작성해 주세요.")
    private Double targetAmount = 0.0;

    @NotEmpty(message = "단위를 체크해 주세요.")
    private final String targetUnit;

    private final List<ApiProcessDto> selectedProcesses;

    public User toEntity() {
        return User.builder()
                .id(id)
                .productName(productName)
                .targetAmount(targetAmount)
                .targetUnit(targetUnit)
                .build();
    }

    @Builder
    public ApiUserDto(Long id, String productName, Double targetAmount, String targetUnit, List<ApiProcessDto> selectedProcesses) {
        this.id = id;
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.targetUnit = targetUnit;
        this.selectedProcesses = selectedProcesses;
    }
}
