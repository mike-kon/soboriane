package com.mikesoft.soboriane.exceptions;

import java.util.FormatterClosedException;
import java.util.IllegalFormatException;

/**
 * Сообщение об ошибке.
 */
public class MessageException extends RuntimeException {

  private final ExceptionCode exceptionCode;
  private final Object args;

  /**
   * Создание исключения.
   *
   * @param exceptionCode текст.
   * @param args          параметры.
   */
  public MessageException(ExceptionCode exceptionCode, Object... args) {
    this.exceptionCode = exceptionCode;
    this.args = args;
  }

  @Override
  public String getMessage() {
    try {
      return args == null
          ? exceptionCode.getTemplateMessage()
          : exceptionCode.getTemplateMessage().formatted(args);
    } catch (IllegalFormatException | FormatterClosedException ex) {
      return exceptionCode.getTemplateMessage() + ". Wrong arguments: " + ex.getMessage();
    }
  }
}
