package com.mikesoft.soboriane.dto.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Структура идентификатора сессии.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionAndUser {
  private String sessionId;
  private String nick;
}
