package com.mikesoft.soboriane.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

  private final MessageService messageService;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {

    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();
    Principal user = accessor.getUser();
    if (user != null ) {
      messageService.userConnect(user.getName());
      log.info("Новое подключение! ID сессии: {}, user: {}", sessionId,  user.getName());
    } else {
      log.warn("Connect, User not defined");
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

    String sessionId = event.getSessionId();
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    Principal user = accessor.getUser();
    if (user != null ) {
      log.info("Отключение! ID сессии: {}, user:{}, причина: {}", sessionId, user.getName(), accessor.getCommand());
      messageService.useerDisconnect(user.getName());
    } else {
      log.warn("Disconnect, User not defined");
    }
  }
}
