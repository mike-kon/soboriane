package com.mikesoft.soboriane.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {

  public void messageUpdate(String nick, String message) {
    log.debug("User:{}, message:{}", nick, message);
    // todo записать в БД
    // todo инициировать STOMP заполнение
  }
}
