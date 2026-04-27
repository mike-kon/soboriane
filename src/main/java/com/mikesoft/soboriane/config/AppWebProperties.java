package com.mikesoft.soboriane.config;

import com.mikesoft.soboriane.enums.RegisterUnknownUsers;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.web")
@Getter
@Setter
public class AppWebProperties {

  private Boolean showDebugElement;
  private RegisterUnknownUsers registerUnknownUsers;
}
