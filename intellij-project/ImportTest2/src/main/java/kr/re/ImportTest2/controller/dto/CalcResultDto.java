package kr.re.ImportTest2.controller.dto;

import kr.re.ImportTest2.domain.CalcResult;
import kr.re.ImportTest2.domain.enumType.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class CalcResultDto {

    private Long id;
    private Long userId;
    private Category category;
    private double resultAmount;
    private String resultAmountUnit;

/*    public CalcResult toEntity() {

    }*/

    @Builder
    public CalcResultDto(Long id, Long userId, Category category, double resultAmount, String resultAmountUnit) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.resultAmount = resultAmount;
        this.resultAmountUnit = resultAmountUnit;
    }
}