package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.service.ResultService;
import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/services/{userId}/result")
public class ResultController {

    private final SelectedProcessService spService;
    private final ResultService resultService;

    @GetMapping("/")
    public String result() {



        return "";
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
