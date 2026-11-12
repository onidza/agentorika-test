package com.example.agentorika.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.example.agentorika.dto.ProviderDto;
import com.example.agentorika.rest.ProviderHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class SubscriberAvailabilityServiceTest {

  private static final String PROVIDER_ID = "provider-1";
  private static final String PHONE = "79991234567";
  private static final String API_KEY = "test-api-key";
  private static final String PROVIDER_URL = "https://provider.example";

  private MockRestServiceServer server;
  private SubscriberAvailabilityService service;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder();
    server = MockRestServiceServer.bindTo(builder).build();
    ProviderRegistry registry = mock(ProviderRegistry.class);
    when(registry.getProvider(PROVIDER_ID))
        .thenReturn(new ProviderDto(PROVIDER_ID, PROVIDER_URL, API_KEY));
    service = new SubscriberAvailabilityService(registry, new ProviderHttpClient(builder.build()));
  }

  @Test
  void returnsTrueWhenProviderRespondsWith200AndOkStatus() {
    server.expect(requestTo(PROVIDER_URL + "/subscribers/" + PHONE))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("X-API-Key", API_KEY))
        .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

    assertTrue(service.isAvailable(PROVIDER_ID, PHONE));

    server.verify();
  }

  @Test
  void returnsFalseWhenProviderRespondsWith404() {
    server.expect(requestTo(PROVIDER_URL + "/subscribers/" + PHONE))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("X-API-Key", API_KEY))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    assertFalse(service.isAvailable(PROVIDER_ID, PHONE));

    server.verify();
  }
}
