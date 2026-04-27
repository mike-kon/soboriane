package com.mikesoft.soboriane.config;

import java.time.Period;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.user")
@Getter
@Setter
public class UserProperties {

  private int dayToRegistration;
  private PasswordSettings passwordSettings;

  @Getter
  @Setter
  public static class PasswordSettings {

    /** Контролировать сложность пароля. */
    private boolean isControl;

    /** Минимальная длина пароля. */
    private int minLenPassword;

    /** Минимальное кол-во букв в другом регистре. */
    private int otherRegisterCount;

    /** Минимальное кол-во цифр. */
    private int minDigital;

    /** Минимальное кол-во знаков препинания. */
    private int minPunctuation;

    /** Время жизни пароля. */
    private Period passwordLive;
  }
}
