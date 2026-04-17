package com.mikesoft.soboriane.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Известные типы сообщений об ошибке.
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  E001("Неавторизованное подключение запрещено."),
  E002("Пользователь %s уже подключился ранее."),
  E003("Сессия с параметром %s не найдена")
  ;
  private final String templateMessage;
}
