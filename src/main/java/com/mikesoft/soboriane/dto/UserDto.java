package com.mikesoft.soboriane.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Пользователь.
 */
@Getter
@Setter
public class UserDto {
  private String nick;
  private String name;
  private String family;
  private LocalDate birthday;
  private LocalDate angelDays;
  private Integer number;
  private Boolean isAdmin;
  private LocalDate dayBegin;
  private LocalDate dayEnd;
}
