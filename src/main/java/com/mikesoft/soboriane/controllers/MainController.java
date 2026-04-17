package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Основной контроллер.
 */
@Controller
@RequestMapping("/")
@Slf4j
public class MainController {

  /**
   * Контроллер index.
   *
   * @param userLogin - логин.
   * @param model - модель для формирования thymeleaf модели.
   * @return - thymeleaf модель.
   */
  @GetMapping("/")
  public String index(@AuthenticationPrincipal UserLoginDto userLogin, Model model) {
    return "index";
  }

  /**
   * Авторизация.
   *
   * @return - thymeleaf модель.
   */
  @GetMapping("login")
  public String login() {
    return "login";
  }

}
