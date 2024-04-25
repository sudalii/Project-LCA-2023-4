package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
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
    private Long category;

    private double resultAmount;

    // 연관관계 Method
/*    public void setUser(User user) {
        this.user = user;
        user.getCalcResults().add(this);
    }*/

    @Builder
    public CalcResult(Long id, User user, Long category, double resultAmount) {
        this.id = id;
        this.category = category;
        this.resultAmount = resultAmount;

        // 연관관계 설정
        this.user = user;
        user.getCalcResults().add(this);
    }
}
