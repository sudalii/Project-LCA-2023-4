package kr.re.ImportTest2.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.re.ImportTest2.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserDto {

    private Long id;

    @NotEmpty(message = "제품 이름을 작성해 주세요.")
    private final String productName;

    @NotNull(message = "제품양을 작성해 주세요.")
    private Double targetAmount = 0.0;

    @NotEmpty(message = "단위를 체크해 주세요.")
    private final String targetUnit;

    public User toEntity() {
        User build = User.builder()
                .id(id)
                .productName(productName)
                .targetAmount(targetAmount)
                .targetUnit(targetUnit)
                .build();
        return build;
    }

    @Builder
    public UserDto(Long id, String productName, Double targetAmount, String targetUnit) {
        this.id = id;
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.targetUnit = targetUnit;
    }
}
