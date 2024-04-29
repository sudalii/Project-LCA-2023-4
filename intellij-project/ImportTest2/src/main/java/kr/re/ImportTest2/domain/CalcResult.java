package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private Category category;

    private double resultAmount;
    private String resultAmountUnit;

    // 연관관계 Method
/*    public void setUser(User user) {
        this.user = user;
        user.getCalcResults().add(this);
    }*/

    @Builder
    public CalcResult(Long id, User user, Category category, double resultAmount, String resultAmountUnit) {
        this.id = id;
        this.category = category;
        this.resultAmount = resultAmount;
        this.resultAmountUnit = resultAmountUnit;

        // 연관관계 설정
        this.user = user;
        user.getCalcResults().add(this);
    }
}
