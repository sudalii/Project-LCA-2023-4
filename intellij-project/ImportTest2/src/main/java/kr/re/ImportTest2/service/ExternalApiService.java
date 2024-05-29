package kr.re.ImportTest2.service;

import kr.re.ImportTest2.component.result.resultTable.CategoryResultTable;
import kr.re.ImportTest2.component.result.resultTable.FlowResultTable;
import kr.re.ImportTest2.component.result.resultTable.ProcessResultTable;
import kr.re.ImportTest2.controller.dto.ApiUserDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CalcResultService resultService;

    public ApiUserDto processAndLogJson(Map<String, Object> payload) {
        try {
            // JSON 전체 로깅
            log.info("Received JSON Payload: {}", objectMapper.writeValueAsString(payload));

            // 각 필드별로 로깅
            String productName = (String) payload.get("productName");
            Double targetAmount = Double.valueOf(payload.get("targetAmount").toString());
            String targetUnit = (String) payload.get("targetUnit");

            log.info("Product Name: {}", productName);
            log.info("Target Amount: {}", targetAmount);
            log.info("Target Unit: {}", targetUnit);

            // 각 필드별로 상세 로깅
            List<Map<String, Object>> selectedProcesses = (List<Map<String, Object>>) payload.get("selectedProcesses");
            for (Map<String, Object> process : selectedProcesses) {
                log.info("Process Name: {}", process.get("processName"));
                log.info("Mapped Process ID: {}", process.get("mappedProcessId"));
                log.info("Process Amount: {}", process.get("processAmount"));
                log.info("Process Amount Unit: {}", process.get("processAmountUnit"));
                log.info("Type: {}", process.get("type"));
                log.info("IFlow1: {}", process.get("iFlow1"));
                log.info("IFlow1 Unit: {}", process.get("iFlow1Unit"));
                log.info("IFlow2: {}", process.get("iFlow2"));
                log.info("IFlow2 Unit: {}", process.get("iFlow2Unit"));
                log.info("OFlow1: {}", process.get("oFlow1"));
                log.info("OFlow1 Unit: {}", process.get("oFlow1Unit"));
            }

            // JSON을 ApiUserDto로 변환
            return objectMapper.convertValue(payload, ApiUserDto.class);

        } catch (Exception e) {
            log.error("Error processing JSON data", e);
            throw new RuntimeException("Error processing JSON data", e);
        }
    }

    public void sendResultTables(List<CategoryResultTable> cgResultTables, String url) {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (CategoryResultTable cgTable : cgResultTables) {
            Map<String, Object> cgMap = new HashMap<>();
            cgMap.put("name", cgTable.name());
            cgMap.put("value", cgTable.value());
            cgMap.put("unit", cgTable.unit());

            List<Map<String, Object>> processJsonList = new ArrayList<>();
            for (ProcessResultTable pTable : cgTable.processResults()) {
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("name", pTable.name());
                pMap.put("value", pTable.value());

                List<Map<String, Object>> flowJsonList = new ArrayList<>();
                for (FlowResultTable fTable : pTable.flowResults()) {
                    Map<String, Object> fMap = new HashMap<>();
                    fMap.put("name", fTable.name());
                    fMap.put("lciResult", fTable.lciResult());
                    fMap.put("cf", fTable.cf());
                    fMap.put("impactResult", fTable.impactResult());
                    flowJsonList.add(fMap);
                }
                pMap.put("flowResults", flowJsonList);
                processJsonList.add(pMap);
            }
            cgMap.put("processResults", processJsonList);
            jsonList.add(cgMap);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(jsonList, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Response: {}", response.getBody());
        } catch (RestClientException e) {
            log.error("Error sending JSON data: ", e);
        }
    }
}