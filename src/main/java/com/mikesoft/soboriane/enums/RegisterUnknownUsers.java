package com.mikesoft.soboriane.enums;

/**
 * Как регистрировать пользователей не из предварительного списка.
 */
public enum RegisterUnknownUsers {

  /** Через подтверждение администратора */
  @Deprecated
  NEED_ACCEPT,

  /** Как и известных */
  AS_KNOWN,

  /** Никак */
  DISABLED
}
