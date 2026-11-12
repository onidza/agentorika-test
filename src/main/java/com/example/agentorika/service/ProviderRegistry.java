package com.example.agentorika.service;

import com.example.agentorika.dto.ProviderDto;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProviderRegistry {

  private volatile Map<String, ProviderDto> registry = Map.of();

  public ProviderRegistry(
      ProviderConfigJsonReader reader,
      @Value("${providers.file-path}") String filePath
  ) {
    replaceProviders(reader.readProviderConfig(Path.of(filePath)));
  }

  public ProviderDto getProvider(String providerId) {
    ProviderDto provider = registry.get(providerId);

    if (provider == null) {
      log.warn("Provider with id {} not found", providerId);
      throw new IllegalArgumentException(
          "Provider not found: " + providerId
      );
    }

    return provider;
  }

  public void replaceProviders(List<ProviderDto> providers) {
    log.info("Replacing {} providers.", providers.size());

    registry = providers.stream()
        .collect(Collectors.toUnmodifiableMap(
            ProviderDto::id,
            Function.identity()
        ));
  }
}
