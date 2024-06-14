package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.component.result.resultTable.ProcessResultTable;
import kr.re.ImportTest2.controller.api.HttpTimeMeasurement;
import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.service.CalcResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/services/{userId}/result")
public class ResultController {

    private final CalcResultService resultService;
    private final ObjectMapper mapper;

    @GetMapping("")
    public String result(@PathVariable("userId") Long userId,
                         Model model) { // , CalcResultDto resultDto

        Pair<CategoryResultTable, List<FlowResultTable>> pair = null;
        UserDto userDto = resultService.findUser(userId);
        List<CalcResultDto> results = resultService.findResultDtos(userId);
        if (results.size() < 5) { // 환경영향범주 5개 미만 저장되어 있을 때 계산수행
            for (CalcResultDto re : results) {
                resultService.deleteResult(re.getId());
            }
            resultService.calculate(userId);
            for (Category cg : Category.values()) {
                switch (cg.getOriName()) {
                    case "Global warming (GWP100a)":
                    case "Abiotic depletion":
                    case "Human toxicity":
                    case "Eutrophication":
                        pair = resultService.resultCml(cg.getOriName());
                        log.info("{} pair: {}", cg.getOriName(), pair);
                        resultService.saveResults(pair.getLeft(), pair.getRight());
                        break;
                    case "Water use":
                        pair = resultService.resultAware(cg.getOriName());
                        log.info("{} pair: {}", cg.getOriName(), pair);
                        resultService.saveResults(pair.getLeft(), pair.getRight());
                        break;
                    default:
                        log.info("pair save: {}, {}", cg.getOriName(), pair);
                }
            }
            results = resultService.findResultDtos(userId);
        }

        model.addAttribute("user", userDto);
        model.addAttribute("categories", Category.values());
        log.info("Category.values(): {}", (Object) Category.values());

        model.addAttribute("results", results);
        try {   // Result 값을 JS로 넘기려면 JSON으로 변환해야 한다고 함
            for (CalcResultDto result : results) {
                switch (result.getCategoryName()) {
                    case "지구온난화":
                        model.addAttribute("GWPResult", mapper.writeValueAsString(result.getResults()));
                        model.addAttribute("GWPFlows", mapper.writeValueAsString(result.getFlowTables()));
                    case "물 사용":
                        model.addAttribute("WUResult", mapper.writeValueAsString(result.getResults()));
                        model.addAttribute("WUFlows", mapper.writeValueAsString(result.getFlowTables()));
                    case "자원고갈":
                        model.addAttribute("ARDResult", mapper.writeValueAsString(result.getResults()));
                        model.addAttribute("ARDFlows", mapper.writeValueAsString(result.getFlowTables()));
                    case "인체독성":
                        model.addAttribute("HTResult", mapper.writeValueAsString(result.getResults()));
                        model.addAttribute("HTFlows", mapper.writeValueAsString(result.getFlowTables()));
                    case "부영양화":
                        model.addAttribute("EPResult", mapper.writeValueAsString(result.getResults()));
                        model.addAttribute("EPFlows", mapper.writeValueAsString(result.getFlowTables()));
                }
                List<ProcessResultTable> processResults = result.getResults().processResults();
                for (ProcessResultTable prt : processResults) {
                    prt.name();
                    prt.value();
                    result.getResults().unit();
                }
            }
/*            String resultsJson = mapper.writeValueAsString(result);
            model.addAttribute("resultsJson", resultsJson);*/
        } catch (JsonProcessingException e) {
            log.error("Error converting results to JSON", e);
        }

/*        try {   // Result 값을 JS로 넘기려면 JSON으로 변환해야 한다고 함
            String resultsJson = objectMapper.writeValueAsString(result);
            model.addAttribute("resultsJson", resultsJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting results to JSON", e);
        }*/

        return "services/result";
    }

/*    @GetMapping("/list")
    public ResponseEntity<Map<String, List<CalcResultDto>>> list(@PathVariable("userId") Long userId) {
        log.info("Returning list by type as JSON");
        Map<String, List<CalcResultDto>> products = new HashMap<>();
        products.put("p2", resultService.findAll(userId));

        return ResponseEntity.ok(products);
    }*/

}
