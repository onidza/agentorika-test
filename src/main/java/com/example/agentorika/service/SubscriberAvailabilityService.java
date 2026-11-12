package com.example.agentorika.service;

import com.example.agentorika.dto.ProviderDto;
import com.example.agentorika.registry.ProviderRegistry;
import com.example.agentorika.rest.ProviderHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriberAvailabilityService {

  private final ProviderRegistry providerRegistry;
  private final ProviderHttpClient providerHttpClient;

  public boolean isAvailable(String providerId, String phone) {
    ProviderDto provider = providerRegistry.getProvider(providerId);
    return providerHttpClient.isAvailable(provider, phone);
  }
}
