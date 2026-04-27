package com.mikesoft.soboriane.dto.web;

import static com.mikesoft.soboriane.util.PhoneUtil.FULL_PHONE_PATTERN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Пользователь.
 */
@Getter
@Setter
@ToString
public class RegisterUserDto {

  private static final String LATIAN = "^[a-z]*$";
  private static final String CYRILLIC_NAME = "^[А-Я][а-я]*$";
  private static final String CYRILLIC_FAMILY = "^[А-Я][а-я]*(-[А-Я][а-я]*)*$";

  @NotBlank(message = "Логин не может быть пустым")
  @Pattern(regexp = LATIAN, message = "Пожалуйста используйте для логина маленькие латинские буквы")
  private String nick;

  @NotBlank(message = "Имя не может быть пустым")
  @Pattern(regexp = CYRILLIC_NAME, message = "Имя надо писать по русски и с большой буквы.")
  private String name;

  @NotBlank(message = "Фамилия не может быть пустой")
  @Pattern(regexp = CYRILLIC_FAMILY, message = "Фамилию надо писать по русски и с большой буквы.")
  private String family;

  @NotEmpty(message = "Пожалуйста, введите телефон.")
  @Pattern(regexp = FULL_PHONE_PATTERN, message
      = "Введите телефон в формате +7 987-654-32-10 или 8(987)654-32-10")
  private String phone;

  @NotBlank(message = "Email нужен для создания и смены пароля.")
  @Email(message = "Введите корректный Email")
  private String email;

  @NotNull(message = "День рождения необходим хотя-бы, чтобы не забыть Вас поздравить.")
  @DateTimeFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthday;
  @DateTimeFormat(pattern = "dd.MM.yyyy")
  private LocalDate angelDays;

  @Min(value = 1, message = "В Псалтире кафизмы начинаются с 1.")
  @Max(value = 20, message = "В Псалтире всего 20 кафизм.")
  private Integer number;
}
