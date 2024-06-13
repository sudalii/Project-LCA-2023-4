package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.component.util.CategoryResultConverter;
import kr.re.ImportTest2.component.util.FlowResultTableListConverter;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "calc_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalcResult {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "calc_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // category type: Long (Id) or String (name)
/*    @Enumerated(EnumType.STRING)
    private Category category;*/
    private String categoryName;

    @Convert(converter = CategoryResultConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private CategoryResultTable results;

    @Convert(converter = FlowResultTableListConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private List<FlowResultTable> flowTables; // 각 category의 가장 많은 양 flow top 3

    @Builder
    public CalcResult(Long id, User user, String categoryName, CategoryResultTable results, List<FlowResultTable> flowTables) {
        this.id = id;
        this.categoryName = categoryName;
        this.results = results;
        this.flowTables = flowTables;

        // 연관관계 설정
        this.user = user;
        user.getCalcResults().add(this);
    }
}
