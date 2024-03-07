package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.domain.ProcessType;
import kr.re.ImportTest2.domain.SelectedProcess;
import kr.re.ImportTest2.domain.UserFlows;
import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class SelectedProcessController {

    private final SelectedProcessService dbService;

/*    @PostMapping("/services")
    public String createProcess(@RequestParam("koreaDbName") List<String> koreaDbNames,
                                @RequestParam("processAmount") List<Double> processAmount) throws IOException {

        dbService.runDb();

        int len = koreaDbNames.size();
        for (int i=0; i<len; i++) {
            // db로 저장

            String pId = dbService.dbMapper(koreaDbNames.get(i));
            double pAmount = processAmount.get(i);
            dbService.addProcess(pId, pAmount);
//            dbService.saveSelectedProcess();
        }
        dbService.systemBuilder();
        dbService.closeDb();

        return "redirect:/result";
    }*/

    /**
     * thymeleaf 에서 데이터 받아오고, db에 저장한다.
     * @PathVariable() vs @RequestParam() 찾기 - 무엇을 적용해야 할지.
     * save vs update 차이 분석
     */
    @PostMapping("/services/{type}/")
    public String createProcess(@RequestParam("dbName") String dbName,
                                @RequestParam("type") String type,
                                SelectedProcess sp, UserFlows flows) {

        String pId = dbService.dbMapper(dbName);

        flows.updateUserFlows(flows.getFlow1(), flows.getFlow1Unit(),
                flows.getFlow2(), flows.getFlow2Unit());
        sp.updateSelectedProcess(sp.getProcessName(), Long.valueOf(pId),
                sp.getProcessAmount(), flows, ProcessType.valueOf(type));

        dbService.saveSelectedProcess(sp);

        return "result/{}";
    }

    @PostMapping("/services/{type}/add")
    public String addProcess(@RequestParam("dbName") String dbName,
                             @RequestParam("type") String type,
                             SelectedProcess sp, UserFlows flows) {

        String pId = dbService.dbMapper(dbName);

        flows.updateUserFlows(flows.getFlow1(), flows.getFlow1Unit(),
                flows.getFlow2(), flows.getFlow2Unit());
        sp.updateSelectedProcess(sp.getProcessName(), Long.valueOf(pId),
                sp.getProcessAmount(), flows, ProcessType.valueOf(type));

        dbService.saveSelectedProcess(sp);

        return "result/{type}";
    }

    @GetMapping("/services/{type}/update")
    public String updateProcess(@PathVariable("id") Long id,
                                @RequestParam("dbName") String dbName,
                                @RequestParam("type") String type,
                                SelectedProcess sp, UserFlows flows) {

        SelectedProcess p = dbService.findOne(id);

    }

    @PostMapping("/services/{type}/delete")
    public String deleteProcess(@PathVariable("type") ProcessType type,
                                @PathVariable("processId") Long id) {   // id 부분 수정

        dbService.deleteSelectedProcess(id);
        return "redirect:/services/{type}";
    }

}