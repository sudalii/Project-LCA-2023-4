package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 현재는 개념이 아래와 동일함
 * User == Product
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

//    @NotNull(message = "제품양을 작성해 주세요.")
    @Column(name = "target_amount")
    private Double targetAmount;

    @Column(name = "target_unit")
    private String targetUnit;

    @OneToMany(mappedBy = "user")
    private List<SelectedProcess> selectedProcesses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CalcResult> calcResults = new ArrayList<>();

    @Builder
    public User(Long id, String productName, Double targetAmount, String targetUnit) {
        this.id = id;
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.targetUnit = targetUnit;
    }

}
