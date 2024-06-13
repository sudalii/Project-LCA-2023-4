package kr.re.ImportTest2.controller.dto;

import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.domain.CalcResult;
import kr.re.ImportTest2.domain.User;
import kr.re.ImportTest2.domain.enumType.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class CalcResultDto {

    private Long id;
    private Long userId;
    private String categoryName;

    private CategoryResultTable results;

    private List<FlowResultTable> flowTables;

    public CalcResult toEntity(User user, CategoryResultTable cgTable, List<FlowResultTable> flowTables) {
        CalcResult result = CalcResult.builder()
                .id(id)
                .user(user)
                .categoryName(Category.getCategoryName(cgTable.name()))
                .results(cgTable)
                .flowTables(flowTables)
                .build();

        return result;
    }

    @Builder
    public CalcResultDto(Long id, Long userId, String categoryName, CategoryResultTable results, List<FlowResultTable> flowTables) {
        this.id = id;
        this.userId = userId;
        this.categoryName = categoryName;
        this.results = results;
        this.flowTables = flowTables;
    }
}
