package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SelectedProcessController {

    private final SelectedProcessService dbService;

    @PostMapping("/services")
    public String createProcess(@RequestParam("koreaDbName") List<String> koreaDbNames,
                                @RequestParam("processAmount") List<Double> processAmount
    ) throws IOException {

        dbService.runDb();

        int len = koreaDbNames.size();
        for (int i=0; i<len; i++) {
            String pId = dbService.dbMapper(koreaDbNames.get(i));
            double pAmount = processAmount.get(i);
            dbService.addProcess(pId, pAmount);
        }
        dbService.systemBuilder();
        dbService.closeDb();

        return "redirect:/result";
    }
}
