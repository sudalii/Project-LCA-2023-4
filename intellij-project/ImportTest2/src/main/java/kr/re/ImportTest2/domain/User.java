package kr.re.ImportTest2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 현재는 개념이 아래와 동일함
 * User == Product
 */
@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String productName;

    private double targetAmount;
    private String targetUnit;

    @OneToMany(mappedBy = "user")
    private List<SelectedProcess> selectedProcesses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CalcResult> calcResults = new ArrayList<>();

    public void updateUser(String productName, double targetAmount, String targetUnit) {
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.targetUnit = targetUnit;
    }
}
