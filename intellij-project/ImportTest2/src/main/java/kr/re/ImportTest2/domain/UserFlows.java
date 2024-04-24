package kr.re.ImportTest2.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
public class UserFlows {

    private double iFlow1;
    private String iFlow1Unit;
    private double iFlow2;
    private String iFlow2Unit;
    private double oFlow1;
    private String oFlow1Unit;

/*    protected UserFlows() {
    }*/

    @Builder
    public UserFlows(double iFlow1, String iFlow1Unit, double iFlow2, String iFlow2Unit, double oFlow1, String oFlow1Unit) {
        this.iFlow1 = iFlow1;
        this.iFlow1Unit = iFlow1Unit;
        this.iFlow2 = iFlow2;
        this.iFlow2Unit = iFlow2Unit;
        this.oFlow1 = oFlow1;
        this.oFlow1Unit = oFlow1Unit;
    }

    public UserFlows() {
    }
}
