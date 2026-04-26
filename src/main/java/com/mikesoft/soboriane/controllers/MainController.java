package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.web.UserLoginDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
  public String login(HttpSession session, Model model) {
    Object attr = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    if (attr instanceof AuthenticationException authenticationException) {
      String msgText;
      Authentication authenticationRequest = authenticationException.getAuthenticationRequest();
      String user = authenticationRequest != null
          ? (String) authenticationException.getAuthenticationRequest().getPrincipal()
          : null;
      switch (authenticationException) {
        case DisabledException _ -> msgText
            = "Пользователь заблокирован. Свяжитесь с администрацией.";
        case CredentialsExpiredException _ -> {
          log.warn("Пора пользователю {} сменить пароль", user);
          model.addAttribute("user", user);
          // TODO: Смена пароля.
          return "uc";
        }
        case LockedException _ -> {
          log.warn("У пользователя {} процедура регистрации не закончена", user);
          // TODO: Определение стадии регистрации и продолжение ее.
          return "uc";
        }
        default -> msgText = authenticationException.getMessage();
      }
      log.debug("!!! {} {} !!!", authenticationException.getClass(), msgText);
      model.addAttribute("loginError", msgText);
    }
    return "login";
  }

}
