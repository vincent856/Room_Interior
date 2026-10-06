package vincent.project_interior.infra.externalapi.gemini.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class GeminiConfig {

  @Bean
  public RestClient geminiRestClient(
      RestClient.Builder builder,
      @Value("${gemini.base-url}") String baseUrl,
      @Value("${gemini.api-key}") String apiKey) {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.ofSeconds(10));
    requestFactory.setReadTimeout(Duration.ofSeconds(120));

    return builder
        .baseUrl(baseUrl)
        .requestFactory(requestFactory)
        .defaultHeader("x-goog-api-key", apiKey)
        .build();
  }
}
