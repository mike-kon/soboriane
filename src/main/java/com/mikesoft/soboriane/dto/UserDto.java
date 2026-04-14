package com.mikesoft.soboriane.dto;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Getter
@Setter
public class UserDto {
  private String nick;
  private String name;
  private String family;
  private LocalDate birthday;
  private LocalDate angel_days;
  private Integer number;
  private Boolean is_admin;
  private LocalDate day_begin;
  private LocalDate day_end;
}
