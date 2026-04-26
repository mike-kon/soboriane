package com.mikesoft.soboriane.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

/**
 * Загрузка списка предЗарегистрированных пользователей.
 */
@Service
@Slf4j
public class LoadPreRegister {

  private static final String RRE_REGISTER_FILE = "preregister.yml";

  private final Map<String, List<LoadPreRegister.DataItem>> preRegData;

  /**
   * Конструктор с загрузкой.
   */
  public LoadPreRegister() {
    Map<String, List<LoadPreRegister.DataItem>> loaded = null;
    try {
      Resource resource = new ClassPathResource(RRE_REGISTER_FILE);
      if (resource.exists()) {
        try (InputStream is = resource.getInputStream()) {
          Yaml yaml = new Yaml();
          loaded = yaml.load(is);
        }
      }
    } catch (Exception ex) {
      log.warn("Ошибка при загрузке файла {}: {}", RRE_REGISTER_FILE, ex.getMessage(), ex);
    } finally {
      preRegData = loaded != null ? loaded : Map.of();
    }
  }

  public List<LoadPreRegister.DataItem> getData() {
    return preRegData.get("data");
  }

  /**
   * Вспомогательный класс для загрузки предрегистрированных пользователей.
   */
  @Getter
  @Setter
  public static class DataItem {
    private String name;
    private String family;
    private LocalDate birthday;
    private String phone;
  }

}
