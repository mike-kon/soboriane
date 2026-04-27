package com.mikesoft.soboriane.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Загрузка списка предЗарегистрированных пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoadPreRegister {

  private static final String RRE_REGISTER_FILE = "preregister.json";
  private static final TypeReference<List<LoadPreRegister.DataItem>> listReference = new TypeListReference();
  private final ObjectMapper objectMapper;

  @Getter
  private List<LoadPreRegister.DataItem> preRegData;

  @PostConstruct
  public void init() {
    List<LoadPreRegister.DataItem> loaded = null;
    try {
      Resource resource = new ClassPathResource(RRE_REGISTER_FILE);
      if (resource.exists()) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
          loaded = objectMapper.readValue(reader, listReference);
        }
      }
    } catch (Exception ex) {
      log.warn("Ошибка при загрузке файла {}: {}", RRE_REGISTER_FILE, ex.getMessage(), ex);
    } finally {
      preRegData = loaded != null ? loaded : List.of();
    }
  }

  /**
   * Вспомогательный класс для загрузки предрегистрированных пользователей.
   */
  @Getter
  @Setter
  public static class DataItem {
    private String name;
    private String family;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;
    private String phone;
  }

  private static class TypeListReference extends TypeReference<List<DataItem>> {
  }
}
