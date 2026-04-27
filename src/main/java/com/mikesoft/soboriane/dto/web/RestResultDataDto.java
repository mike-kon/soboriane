package com.mikesoft.soboriane.dto.web;

import lombok.Getter;

/**
 * Результат REST запроса.
 *
 * @param <T> Тип возвращаемого ответа.
 */
@Getter
public class RestResultDataDto<T> extends RestResultDto {
  private final T data;

  /**
   * Сообщение об успешном исполнении.
   *
   * @param t данные.
   */
  public RestResultDataDto(T t) {
    data = t;
  }

  /**
   * Сообщение об ошибке.
   *
   * @param errorCode код ошибки.
   * @param errorText текст ошибки.
   */
  public RestResultDataDto(String errorCode, String errorText) {
    data = null;
    super(errorCode, errorText);
  }

  /**
   * TimeOut.
   */
  public RestResultDataDto(long time) {
    data = null;
    super(time);
  }
}
