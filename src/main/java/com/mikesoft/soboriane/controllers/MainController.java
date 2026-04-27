package com.mikesoft.soboriane.controllers;

import static com.mikesoft.soboriane.controllers.RegisterController.NEED_ACCEPT_MODEL_ATTRIBUTE;
import static com.mikesoft.soboriane.controllers.RegisterController.NICK_MODEL_ATTRIBUTE;
import static com.mikesoft.soboriane.controllers.RegisterController.SESSION_ID_MODEL_ATTRIBUTE;
import static com.mikesoft.soboriane.controllers.RegisterController.SET_PASSWORD_THYMELEAF;
import static com.mikesoft.soboriane.util.MyWebUtils.addDebugElement;
import com.mikesoft.soboriane.config.AppWebProperties;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Основной контроллер.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {

  private final AppWebProperties appWebProperties;

  /**
   * Контроллер index.
   *
   * @return - thymeleaf модель.
   */
  @GetMapping("/")
  public String index() {
    return "index";
  }

  /**
   * Авторизация.
   *
   * @return - thymeleaf модель.
   */
  @GetMapping("login")
  public String login(HttpSession session, Model model) {
    addDebugElement(appWebProperties.getShowDebugElement(), model, session.getId());
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
        case CredentialsExpiredException _,
             LockedException _ -> {
          log.warn("Пора пользователю {} сменить пароль", user);
          model.addAttribute(NICK_MODEL_ATTRIBUTE, user);
          model.addAttribute(SESSION_ID_MODEL_ATTRIBUTE, session.getId());
          model.addAttribute(NEED_ACCEPT_MODEL_ATTRIBUTE, false);
          return SET_PASSWORD_THYMELEAF;
        }
        default -> msgText = authenticationException.getMessage();
      }
      log.debug("!!! {} {} !!!", authenticationException.getClass(), msgText);
      model.addAttribute("loginError", msgText);
    }
    return "login";
  }

}
