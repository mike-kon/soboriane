package com.mikesoft.soboriane.services;

import com.mikesoft.soboriane.config.ClientProperties;
import com.mikesoft.soboriane.dto.MessageDto;
import com.mikesoft.soboriane.dto.UserDto;
import com.mikesoft.soboriane.util.UuidV7;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.mikesoft.soboriane.enums.MessageType.MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  private final ClientProperties clientProperties;
  private final SimpMessagingTemplate messagingTemplate;
  private final SpringTemplateEngine templateEngine;

  private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();
  private final Set<MessageDto> cacheMessages = ConcurrentHashMap.newKeySet();

  @Async
  public void messageUpdate(UserDto user, String message) {
    String nick = user.getNick();
    log.debug("User:{}, message:{}", nick, message);
    // создать сообщение.
    MessageDto messageDto = messageGenerate(user, message);
    cacheMessages.add(messageDto);
    // todo записать в БД.
    // todo инициировать STOMP заполнение.
    connectedUsers.forEach(this::sendHtmlContentToUser);
  }

  public void userConnect(String sessionId) {
    connectedUsers.add(sessionId);
  }

  public void useerDisconnect(String sessionId) {
    connectedUsers.remove(sessionId);
  }

  /**
   * Генерирует объект MessageDto из сообщения.
   *
   * Пока реализовано просто сообщение.
   * todo Реализовать все типы сообщений
   * @param user пользователь.
   * @param message исходное сообщение.
   * @return MessageDto
   */
  private MessageDto messageGenerate(UserDto user, String message) {
    return new MessageDto(UuidV7.generate(), LocalDateTime.now(), user, MESSAGE, message, null);
  }

  private void sendHtmlContentToUser(String user) {
    String htmlMessages = cacheMessages.stream()
        .map(msg -> generateHtml(user, msg))
        .collect(Collectors.joining());
    messagingTemplate.convertAndSendToUser(user, clientProperties.getTopic(), htmlMessages);
  }

  private String generateHtml(String user, MessageDto message) {
    Context context = new Context();
    UUID idMessage = message.getId();
    context.setVariable("idMessage", idMessage);
    Boolean isOpponent = !user.equals(message.getOwner().getNick());
    context.setVariable("isOpponent", isOpponent);
    String opponentName = message.getOwner().getName() + " " + message.getOwner().getFamily();
    context.setVariable("opponentName",opponentName);
    String messageText = message.getMessage();
    context.setVariable("message", messageText);
    return templateEngine.process("partmessages", context);
  }
}
