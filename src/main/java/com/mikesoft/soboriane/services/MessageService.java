package com.mikesoft.soboriane.services;

import static com.mikesoft.soboriane.enums.MessageType.MESSAGE;
import static com.mikesoft.soboriane.util.UuidV7.generate;

import com.mikesoft.soboriane.config.ClientProperties;
import com.mikesoft.soboriane.dto.web.SessionAndUser;
import com.mikesoft.soboriane.dto.db.MessageDto;
import com.mikesoft.soboriane.dto.db.UserDto;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Сервис сообщений.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  private final ClientProperties clientProperties;
  private final SimpMessagingTemplate messagingTemplate;
  private final SpringTemplateEngine templateEngine;
  private final UserSession userSessions;

  private final Set<MessageDto> cacheMessages = ConcurrentHashMap.newKeySet();
  private static final DateTimeFormatter FORMAT_TIME_MARK =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  /**
   * Инициализация сервиса.
   */
  @PostConstruct
  public void init() {
    MessageDto startMessage = new MessageDto();
    startMessage.setMessage("Start");
    UserDto systemUser = new UserDto();
    systemUser.setName("System");
    systemUser.setFamily("");
    systemUser.setNick("system");
    systemUser.setIsAdmin(false);
    startMessage.setOwner(systemUser);
    startMessage.setId(generate());
    startMessage.setType(MESSAGE);
    startMessage.setCreated(LocalDateTime.now());
    cacheMessages.add(startMessage);
  }

  /**
   * Обработка входящего сообщения.
   *
   * @param user - отправитель.
   * @param message - сообщение.
   */
  @Async
  public void messageSend(UserDto user, String message) {
    String nick = user.getNick();
    log.debug("User:{}, message:{}", nick, message);
    // создать сообщение.
    MessageDto messageDto = messageGenerate(user, message);
    cacheMessages.add(messageDto);
    // TODO: записать в БД.
    messageUpdate();
  }

  /**
   * Команда на обновление всех клиентов.
   */
  @Async
  public void messageUpdate() {
    userSessions.getConnected().forEach(this::sendHtmlToSession);
  }

  /**
   * Генерирует объект MessageDto из сообщения.
   * Пока реализовано просто сообщение.
   * TODO: Реализовать все типы сообщений
   *
   * @param user    пользователь.
   * @param message исходное сообщение.
   * @return MessageDto
   */
  private MessageDto messageGenerate(UserDto user, String message) {
    return new MessageDto(generate(), LocalDateTime.now(), user, MESSAGE, message, null);
  }

  private void sendHtmlToSession(SessionAndUser session) {
    String htmlMessages = cacheMessages.stream()
        .sorted(Comparator.comparing(MessageDto::getCreated))
        .map(msg -> generateHtml(session.getNick(), msg))
        .collect(Collectors.joining());
    messagingTemplate.convertAndSendToUser(session.getNick(), clientProperties.getTopic(),
        htmlMessages);
  }

  private String generateHtml(String user, MessageDto message) {
    Context context = new Context();
    UUID idMessage = message.getId();
    context.setVariable("idMessage", idMessage);
    Boolean isOpponent = !user.equals(message.getOwner().getNick());
    context.setVariable("isOpponent", isOpponent);
    String opponentName = message.getOwner().getName() + " " + message.getOwner().getFamily() + " ";
    context.setVariable("opponentName", opponentName);
    context.setVariable("timeMark", message.getCreated().format(FORMAT_TIME_MARK));
    context.setVariable("message", message.getMessage());
    return templateEngine.process("partmessages", context);
  }
}
