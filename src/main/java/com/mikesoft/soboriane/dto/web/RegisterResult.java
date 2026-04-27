package com.mikesoft.soboriane.dto.web;

import lombok.Getter;

/**
 * Результат регистрации.
 */
@Getter
public class RegisterResult {

  private final RegisterResultType type;
  private final String addInfo;

  /**
   * Формирование кода возврата.
   *
   * @param type код возврата.
   */
  public RegisterResult(RegisterResultType type) {
    this.type = type;
    this.addInfo = null;
  }

  /**
   * Формирование кода возврата совместно с дополнительной информацией.
   *
   * @param type код возврата.
   */

  public RegisterResult(RegisterResultType type, String addInfo) {
    this.type = type;
    this.addInfo = addInfo;
  }

  /**
   * Тип результата регистрации.
   */
  public enum RegisterResultType {
    /** Пользователь нормально зарегистрирован. */
    REGISTERED_SUCCESS,

    /** Пользователь зарегистрирован, но требуется подтверждение. */
    PRE_REGISTERED,

    /** Такой пользователь уже есть. */
    USER_EXISTS,

    /** Некоторые поля уже встречаются. */
    DUPLICATE_FIELD,

    /** Надо продолжить регистрацию. Нужен пароль */
    CONTINUE_WITH_PASSWORD,

    /** Регистрация просрочена. */
    REGISTRATION_OVERDUE
  }
}
