package kr.re.ImportTest2.controller;

import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.controller.dto.CalcResultDto;
import kr.re.ImportTest2.controller.dto.ProcessDto;
import kr.re.ImportTest2.controller.dto.UserDto;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.service.CalcResultService;
import kr.re.ImportTest2.service.SelectedProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final SelectedProcessService spService;
    private final CalcResultService resultService;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public String result(@PathVariable("userId") Long userId,
                         Model model) { // , CalcResultDto resultDto

        UserDto userDto = resultService.findUser(userId);
        resultService.calculate(userId);
        List<CategoryResultTable> result = resultService.result();

        model.addAttribute("user", userDto);
        model.addAttribute("categories", Category.values());

        try {   // Result 값을 JS로 넘기려면 JSON으로 변환해야 한다고 함
            String resultsJson = objectMapper.writeValueAsString(result);
            model.addAttribute("resultsJson", resultsJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting results to JSON", e);
        }

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
