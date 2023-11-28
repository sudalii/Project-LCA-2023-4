package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calc_result")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalcResult {

    @Id
    @GeneratedValue
    @Column(name = "calc_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // category type: Long (Id) or String (name)
    private Long category;

    private double resultAmount;

    // 연관관계 Method
    public void setUser(User user) {
        this.user = user;
        user.getCalcResults().add(this);
    }


}
