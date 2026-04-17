package com.mikesoft.soboriane;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Генерирован автоматически.
 */
public class ServletInitializer extends SpringBootServletInitializer {

  /**
   * Генерирован автоматически.
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SoborianeApplication.class);
  }

}
