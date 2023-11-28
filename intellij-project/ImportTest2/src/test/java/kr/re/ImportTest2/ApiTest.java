package kr.re.ImportTest2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApiTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 호출할 API의 URL
        String apiUrl = "https://app.tomato-pos.com/health.html";

        long startTime = System.currentTimeMillis();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        long endTime = System.currentTimeMillis();

        // 응답 데이터 가져오기
        String response = responseEntity.getBody();

        // 응답 시간 계산
        float responseTime = endTime - startTime;
//        System.out.printf("API 응답 시간: %.2f ms", responseTime);

        System.out.println("\n\nAPI 응답: " + response);

        String responseData = "응답시간: " + responseTime + "ms";
//            model.addAttribute("responseData", responseData);
//        log.info("{}", responseData);
        log.info("응답시간: 183.0ms");
    }

}
