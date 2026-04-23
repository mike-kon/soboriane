package com.mikesoft.soboriane.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Пользователь.
 */
@Getter
@Setter
public class RegisterUserDto {
  private String nick;
  private String name;
  private String family;
  private String phone;
  private String email;
  private LocalDate birthday;
  private LocalDate angelDays;
  private Integer number;
}
