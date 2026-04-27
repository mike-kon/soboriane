package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.web.UserLoginDto;
import com.mikesoft.soboriane.services.MessageService;
import com.mikesoft.soboriane.services.views.MenuView;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Контроллер бизнес-логики.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LogicController {

  private final MessageService messageService;
  private final Map<String, MenuView> menuCommands;

  /**
   * Каманда на загрузку элемента, выбранного из меню.
   *
   * @param command - команда
   * @param user - пользователь.
   * @param model - модель для формирования thymeleaf модели.
   * @return thymeleaf модель.
   */
  @PostMapping("menu")
  public String menu(@RequestParam String command, @AuthenticationPrincipal UserLoginDto user,
                     Model model) {
    MenuView view = menuCommands.get(command);
    return view != null ? view.loadView(user, model) : "uc";
  }

  /**
   * Отправка сообщения на сервер.
   *
   * @param message - сообщение.
   * @param user - пользователь.
   */
  @PostMapping("message")
  @ResponseStatus(HttpStatus.OK)
  public void sendMessage(@RequestParam String message,
                          @AuthenticationPrincipal UserLoginDto user) {
    messageService.messageSend(user, message);
  }

  /**
   * Инициализация блока сообщений.
   * Инициирует отправку всех сообщений только что присоединившемуся пользователю.
   */
  @PostMapping("messageinit")
  @ResponseStatus(HttpStatus.OK)
  public void messageInit() {
    messageService.messageUpdate();
  }

  /**
   * Вывод формы регистрации.
   *
   * @return имя формы регистрации.
   */
  public String register() {
    return "register";
  }
}
