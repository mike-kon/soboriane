package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.RegisterUserDto;
import com.mikesoft.soboriane.dto.web.RestResultDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Обрабатывает результат регистрации.
 */
@RestController
@RequestMapping("/api")
public class RegisterController {

  /**
   * Попытка регистрациинового пользователя.
   *
   * @param user параметры пользователя.
   * @return результат регистрации.
   */
  @PostMapping("createRegister")
  public RestResultDto registerUser (RegisterUserDto user) {
    return null;
  }
}
