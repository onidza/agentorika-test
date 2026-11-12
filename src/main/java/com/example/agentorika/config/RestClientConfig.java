package com.example.agentorika.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();

    JdkClientHttpRequestFactory factory =
        new JdkClientHttpRequestFactory(httpClient);

    factory.setReadTimeout(Duration.ofSeconds(5));

    return RestClient.builder()
        .requestFactory(factory)
        .build();
  }
}
