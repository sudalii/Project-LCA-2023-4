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

    @GetMapping("/result")
    public void calculate() {
        dbService.runDb();

        int len = koreaDbNames.size();
        for (int i=0; i<len; i++) {
            // db로 저장

            String pId = dbService.dbMapper(koreaDbNames.get(i));
            double pAmount = processAmount.get(i);
            dbService.addProcess(pId, pAmount);

            dbService.saveSelectedProcess();
        }
        dbService.systemBuilder();
        dbService.closeDb();

    }

}
