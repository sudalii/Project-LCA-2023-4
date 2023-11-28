package kr.re.ImportTest2.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserForm {

    @NotEmpty(message = "필수 입력란")
    private String productName;

    private double targetAmount;
    private String targetUnit;
}
