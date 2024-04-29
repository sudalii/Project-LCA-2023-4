package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.domain.enumType.ProcessType;
import kr.re.ImportTest2.service.CalcResultService;
import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/services/{userId}/result")
public class ResultController {

    private final SelectedProcessService spService;
    private final CalcResultService resultService;

    @GetMapping("")
    public String result(@PathVariable("userId") Long userId,
                         Model model) {

        String productName = resultService.findUser(userId).getProductName();
        model.addAttribute("productName", productName);

        return "";
    }

/*    @GetMapping("/list")
    public ResponseEntity<Map<String, List<CalcResultDto>>> list(@PathVariable("userId") Long userId) {
        log.info("Returning list by type as JSON");
        Map<String, List<CalcResultDto>> products = new HashMap<>();
        products.put("p2", resultService.findAll(userId));

        return ResponseEntity.ok(products);
    }*/

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
