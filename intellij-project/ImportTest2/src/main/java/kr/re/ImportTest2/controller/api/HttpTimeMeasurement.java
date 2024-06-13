package kr.re.ImportTest2.controller.api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpTimeMeasurement {
    public static long measureRequestResponseTime(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + apiKey) // API 키가 필요할 경우
                .build();

        long startTime = System.nanoTime();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; // 밀리초 단위 변환
            System.out.println("Response time: " + duration + " ms");
            System.out.println("Response status code: " + response.statusCode());
//            System.out.println("Response body: " + response.body());
            return duration;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return 0;
    }
}
