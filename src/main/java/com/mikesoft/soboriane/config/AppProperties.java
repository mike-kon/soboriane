package com.mikesoft.soboriane.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
@Getter
@Setter
public class AppProperties {

  WebSocketConfiguration webSocketConfiguration;

  @Getter
  @Setter
  public static class WebSocketConfiguration {
    private String enableSimpleBroker;
    private String applicationDestinationPrefixes;
    private String updates;
    private String endpoint;
  }

}
