package com.example.agentorika.service;

import com.example.agentorika.dto.ProviderDto;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderConfigJsonReader {

  private final ObjectMapper objectMapper;

  public List<ProviderDto> readProviderConfig(Path path) {
    try {
      return objectMapper.readValue(
          path.toFile(),
          new TypeReference<List<ProviderDto>>() {}
      );
    } catch (JacksonException e) {
      log.warn("Could not read provider config file {}", path, e);

      throw new IllegalStateException(
          "Failed to read providers from " + path, e
      );
    }
  }
}
