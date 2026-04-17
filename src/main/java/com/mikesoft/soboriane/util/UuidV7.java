package com.mikesoft.soboriane.util;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

/**
 * Библиотечный класс. Содержит методы для работы с UUID v7
 */
public class UuidV7 {
  private UuidV7() {}

  /**
   * Генерирует UUID v7.
   *
   * @return UUID.
   */
  public static UUID generate() {
    return UuidCreator.getTimeOrderedEpoch();
  }
}
