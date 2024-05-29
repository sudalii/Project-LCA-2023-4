package kr.re.ImportTest2.controller.api;


import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.controller.dto.ApiUserDto;
import kr.re.ImportTest2.service.CalcResultService;
import kr.re.ImportTest2.service.ExternalApiService;
import kr.re.ImportTest2.service.SelectedProcessService;
import kr.re.ImportTest2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

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


    // curl -X POST "http://<your-ip>:8080/receive-json?sendUrl=http://<receiver-ip>:<receiver-port>/endpoint" -H "Content-Type: application/json" -d '{"key1":"value1","key2":"value2"}'
/*    @PostMapping("/json")
    public ResponseEntity<String> receiveJson(@RequestBody Map<String, Object> payload,
                                              @RequestBody List<CategoryResultTable> cgResultTables,
                                              @RequestParam String sendUrl) {
        // 1. 받은 JSON 데이터 파싱 및 출력
        payload.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
        // 2. 파싱한 데이터 user/selectedProcess dto로 넣어서 저장하기

        // 3. Prepare data to send
        externalApiService.sendResultTables(cgResultTables, sendUrl);

        return ResponseEntity.ok("JSON received and processed successfully");
    }*/

    @PostMapping("/receive-json")
    public ResponseEntity<String> receiveJson(@RequestBody Map<String, Object> payload) {

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
        List<CategoryResultTable> result = resultService.result();
        externalApiService.sendResultTables(result, "http://10.252.107.96:8080/api/result");

        // 생성 후 저장했던 엔티티 데이터 삭제
//        userService.deleteUser(userId);

        return ResponseEntity.ok("JSON received and processed successfully");
    }

    @PostMapping("/result")
    public ResponseEntity<String> receiveJson(@RequestBody List<Map<String, Object>> payload) {
        payload.forEach(category -> {
            System.out.println("Category:");
            category.forEach((key, value) -> {
                System.out.println("  " + key + ": " + value);
            });

            List<Map<String, Object>> processResults = (List<Map<String, Object>>) category.get("processResults");
            processResults.forEach(process -> {
                System.out.println("  Process:");
                process.forEach((key, value) -> {
                    System.out.println("    " + key + ": " + value);
                });

                List<Map<String, Object>> flowResults = (List<Map<String, Object>>) process.get("flowResults");
                flowResults.forEach(flow -> {
                    System.out.println("    Flow:");
                    flow.forEach((key, value) -> {
                        System.out.println("      " + key + ": " + value);
                    });
                });
            });
        });
        return ResponseEntity.ok("JSON received successfully");
    }
/*    @PostMapping("/send-result")
    public ResponseEntity<String> sendResultTables() {
        String url = "http://10.252.107.96:8080/api/receive-json";  // 외부 서버의 URL을 여기에 입력
        externalApiService.sendResultTables(cgResultTables, url);
        return ResponseEntity.ok("Data sent successfully");
    }*/
}