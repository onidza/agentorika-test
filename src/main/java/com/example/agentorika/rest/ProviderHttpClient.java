package com.example.agentorika.rest;

import com.example.agentorika.dto.ProviderDto;
import com.example.agentorika.dto.SubscriberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProviderHttpClient {

  private final RestClient restClient;

  public boolean isAvailable(ProviderDto provider, String phone) {
    try {
      SubscriberResponseDto response = restClient.get()
          .uri(provider.url() + "/subscribers/{phone}", phone)
          .header("X-API-Key", provider.apiKey())
          .retrieve()
          .body(SubscriberResponseDto.class);

      return response != null && "ok".equals(response.status());

    } catch (HttpClientErrorException.NotFound e) {
      log.info("Subscriber unavailable for providerId={}", provider.id());
      return false;
    }
  }
}
