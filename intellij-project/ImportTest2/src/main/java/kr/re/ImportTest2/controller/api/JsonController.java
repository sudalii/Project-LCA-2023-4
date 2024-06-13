package kr.re.ImportTest2.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.controller.dto.ApiUserDto;
import kr.re.ImportTest2.domain.enumType.Category;
import kr.re.ImportTest2.service.CalcResultService;
import kr.re.ImportTest2.service.ExternalApiService;
import kr.re.ImportTest2.service.SelectedProcessService;
import kr.re.ImportTest2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JsonController {

    private final ExternalApiService externalApiService;
    private final UserService userService;
    private final SelectedProcessService spService;
    private final CalcResultService resultService;
    private final ObjectMapper objectMapper;


    @PostMapping("")
    public ResponseEntity<List<Map<String, Object>>> receiveJson(
//    public ResponseEntity<String> receiveJson(
            @RequestBody Map<String, Object> payload,
            HttpSession session) {

        if (!userService.isEmpty())
            userService.deleteAll();

//        ApiUserDto userDto = externalApiService.processAndLogJson(payload);
        ObjectMapper mapper = new ObjectMapper();
        ApiUserDto userDto = mapper.convertValue(payload, ApiUserDto.class);
        log.info("Received UserDto: {}", userDto);  // JSON 데이터 파싱 및 출력

        // UserDto 저장, ProcessDto 목록 저장
        Long userId = userService.saveUserForApi(userDto);
        userDto.getSelectedProcesses().forEach(process -> {
            spService.saveProcessForApi(process, userId);
        });
        // 계산 및 결과 전송
        resultService.calculate(userId);
        List<CategoryResultTable> result = resultService.resultForApi();
        List<Map<String, Object>> jsonList = externalApiService.convertResultToJsonList(result);
//        externalApiService.sendResultTables(result, sendUrl);
        // "http://10.252.107.96:8080/api/result"

        // 생성 후 저장했던 엔티티 데이터 삭제
//        userService.deleteUser(userId);

/*        session.setAttribute("jsonList", jsonList);  // 세션에 결과 저장
        session.setAttribute("userId", userId);      // 사용자 ID 저장
        log.info("json: {}, userID: {}", jsonList, userId);*/

//        return ResponseEntity.ok("JSON received and data prepared for display.");
//        return ResponseEntity.ok("JSON received and processed successfully");
        return ResponseEntity.ok(jsonList);
    }

/*    @GetMapping("/result")
    public String jsonResult(HttpSession session, ApiUserDto userDto, Model model) {
        List<Map<String, Object>> result =
                (List<Map<String, Object>>) session.getAttribute("jsonList");
        Long userId = (Long) session.getAttribute("userId");
        log.info("userId: {}, result: {}", userId, result);
        if (result == null || userId == null) {
            log.error("Result or user ID is null.");
            return "error";  // 오류 페이지를 보여주는 경로를 설정할 수 있습니다.
        }
        model.addAttribute("user", userDto);
        model.addAttribute("userId", userId);
        model.addAttribute("categories", Category.values());
        try {   // Result 값을 JS로 넘기려면 JSON으로 변환해야 한다고 함
            String resultsJson = objectMapper.writeValueAsString(result);
            model.addAttribute("resultsJson", resultsJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting results to JSON", e);
        }

        return "api/result";
    }*/

}