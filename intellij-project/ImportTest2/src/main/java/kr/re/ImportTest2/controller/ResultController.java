package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ResultController {

    private final SelectedProcessService dbService;

    @GetMapping("/result")
    public String result() {



        return "/result";
    }

}
