package com.mikesoft.soboriane.config;

import static com.mikesoft.soboriane.exceptions.ExceptionCode.E001;

import com.mikesoft.soboriane.exceptions.MessageException;
import com.mikesoft.soboriane.services.MessageSession;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * RКонфигурация STOMP-сокета.
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

  private final ClientProperties clientProperties;
  private final MessageSession messageSessions;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(clientProperties.getBrokerPrefix());
    registry.setApplicationDestinationPrefixes(clientProperties.getApplicationPrefix());
    registry.setUserDestinationPrefix(clientProperties.getUserPrefix());
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(clientProperties.getEndpoint())
        .withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Перехватываем STOMP команду CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          Principal principal = accessor.getUser();
          if (principal == null) {
            throw new MessageException(E001);
          }
          String nick = principal.getName();
          log.debug("Попытка подключения {} ", nick);
          // TODO: Добавить эту проверку и обеспечить  ее нормальную обработку на клиенте.
        }
        return message;
      }
    });
  }
}
