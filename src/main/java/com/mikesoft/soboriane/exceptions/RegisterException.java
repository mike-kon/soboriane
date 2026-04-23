package com.mikesoft.soboriane.exceptions;

/**
 * Исключения регистрации.
 */
public class RegisterException extends MessageException {

  public RegisterException(ExceptionCode exceptionCode, Object... args) {
    super(exceptionCode, args);
  }
}
