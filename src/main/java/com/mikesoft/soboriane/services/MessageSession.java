package com.mikesoft.soboriane.services;

import static com.mikesoft.soboriane.exceptions.ExceptionCode.E003;

import com.mikesoft.soboriane.dao.SessionAndUser;
import com.mikesoft.soboriane.exceptions.MessageException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Сессии сообщений.
 */
@Component
public class MessageSession {

  @Getter
  private final Set<SessionAndUser> connected = ConcurrentHashMap.newKeySet();

  /**
   * Сообщение о соединении.
   *
   * @param session сессия и пользователь.
   */
  public void userConnect(SessionAndUser session) {
    connected.add(session);
  }

  /**
   * Сообщение об отключении.
   *
   * @param sessionPar Параметр сессии. Имя пользователя или Id сессии.
   */
  public void userDisconnect(String sessionPar) {
    Optional<SessionAndUser> removeSessionOpt = connected.stream()
        .filter(x -> sessionPar.equals(x.getNick()))
        .findFirst();
    if (removeSessionOpt.isEmpty()) {
      removeSessionOpt = connected.stream()
          .filter(x -> sessionPar.equals(x.getSessionId()))
          .findFirst();
    }
    if (removeSessionOpt.isEmpty()) {
      throw new MessageException(E003, sessionPar);
    } else {
      connected.remove(removeSessionOpt.get());
    }
  }

  /**
   * Проверка на существование пользователя среди  пожключенных.
   *
   * @param nick - пользователь.
   * @return если  true,  то уже подключен.
   */
  public boolean isUserExists(String nick) {
    return connected.stream().anyMatch(x -> nick.equals(x.getNick()));
  }
}
