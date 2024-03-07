package kr.re.ImportTest2.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

@Embeddable
@Getter
public class UserFlows {

    private double flow1;
    private String flow1Unit;
    private double flow2;
    private String flow2Unit;

    protected UserFlows() {
    }

    public void updateUserFlows(double flow1, String flow1Unit, double flow2, String flow2Unit) {
        this.flow1 = flow1;
        this.flow1Unit = flow1Unit;
        this.flow2 = flow2;
        this.flow2Unit = flow2Unit;
    }
}
