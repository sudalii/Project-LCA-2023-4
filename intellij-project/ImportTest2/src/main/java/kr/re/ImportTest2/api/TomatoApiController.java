package kr.re.ImportTest2.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive

@Controller
@Slf4j
@RequiredArgsConstructor
public class TomatoApiController {
    /**
     * Spring 에서의 외부 API 호출 방법
     *   1. RestTemplate() 사용
     *   2. WebClient 사용 (Spring WebFlux 에 포함, Spring 5부터 도입)
     *      --> Spring 6으로 보이는데 라이브러리 적용이 안됨
     */
    @GetMapping("/index")
    public void apiCallWithRestTemplate(Model model) {
        // RestTemplate 객체 생성
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
        System.out.printf("API 응답 시간: %.2f ms", responseTime);

        System.out.println("API 응답: " + response);

        String responseData = "응답시간: " + responseTime + "ms";
        model.addAttribute("responseData", responseData);
        log.info("{}", responseData);

//        return "/";
    }

/*    @GetMapping("/result")
    public String apiCallWithWebClient() {
        // WebClient 객체 생성
        WebClient webClient = WebClient.create();

        // 호출할 API의 URL
        String apiUrl = "https://api.example.com/data";

        // API 호출 및 응답 수신
        String response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // 응답 데이터 처리
        return response;
    }*/

}
