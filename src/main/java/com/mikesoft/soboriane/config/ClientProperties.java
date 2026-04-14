package com.mikesoft.soboriane.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.client-configuration")
@Getter
@Setter
public class ClientProperties {
  private String endpoint;
  private String[] brokerPrefix;
  private String applicationPrefix;
  private String userPrefix;
  private String topic;
  private Integer maxRetryConnect;
  private Long timeOutValue;
}
