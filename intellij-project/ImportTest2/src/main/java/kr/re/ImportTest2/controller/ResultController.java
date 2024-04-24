package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ResultController {

    private final SelectedProcessService spService;

    @GetMapping("/result")
    public String result() {



        return "/result";
    }

/*    @GetMapping("/result")
    public void calculate() {
        spService.runDb();

        int len = lciDbNames.size();
        for (int i=0; i<len; i++) {
            // db로 저장

            String pId = spService.dbMapper(lciDbNames.get(i));
            double pAmount = processAmount.get(i);
            spService.addProcess(pId, pAmount);

            spService.saveProcess();
        }
        spService.systemBuilder();
        spService.closeDb();

    }*/

}
