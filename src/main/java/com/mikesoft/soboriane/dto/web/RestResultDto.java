package com.mikesoft.soboriane.dto.web;

import static com.mikesoft.soboriane.enums.RestResultEnum.ERROR;
import static com.mikesoft.soboriane.enums.RestResultEnum.OK;
import static com.mikesoft.soboriane.enums.RestResultEnum.TIMEOUT;

import com.mikesoft.soboriane.enums.RestResultEnum;
import lombok.Getter;

/**
 * Результат REST запроса.
 */
@Getter
public class RestResultDto {

  private final RestResultEnum restResultEnum;
  private final String errorCode;
  private final String errorText;

  /**
   * Сообщение об успешном исполнении.
   *
   */
  public RestResultDto() {
    restResultEnum = OK;
    errorCode = null;
    errorText = null;
  }

  /**
   * Сообщение об ошибке.
   *
   * @param errorCode код ошибки.
   * @param errorText текст ошибки.
   */
  public RestResultDto(String errorCode, String errorText) {
    restResultEnum = ERROR;
    this.errorCode = errorCode;
    this.errorText = errorText;
  }

  /**
   * TimeOut.
   */
  public RestResultDto(long time) {
    restResultEnum = TIMEOUT;
    errorCode = "TimeOut";
    errorText = "Нет ответа в течении отведенного времени " +  time;
  }
}
