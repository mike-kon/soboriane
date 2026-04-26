package com.mikesoft.soboriane.controllers;

import static com.mikesoft.soboriane.services.RegisterServices.EMAIL_FIELD;
import static com.mikesoft.soboriane.services.RegisterServices.NICK_FIELD;
import static com.mikesoft.soboriane.services.RegisterServices.PHONE_FIELD;
import static com.mikesoft.soboriane.util.PhoneUtil.CANONICAL_PHONE_PATTERN;
import static com.mikesoft.soboriane.util.PhoneUtil.phoneToCanonical;

import com.mikesoft.soboriane.dto.web.RegisterUserDto;
import com.mikesoft.soboriane.enums.RegisterResult;
import com.mikesoft.soboriane.services.RegisterServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Обрабатывает результат регистрации.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

  private static final String EXISTS_IN_DB = "Такой %s уже существует в БД";
  private static final String REGISTER_THYMELEAF = "register";
  private static final String WRONG_THYMELEAF = "wrong";
  private static final String SET_PASSWORD_THYMELEAF = "setpassword";

  private final RegisterServices registerServices;

  /**
   * Авторизация.
   *
   * @return - thymeleaf модель.thymeleaf модель.
   */
  @GetMapping(REGISTER_THYMELEAF)
  public String register(Model model) {
    model.addAttribute("registerUserDto", new RegisterUserDto());
    return REGISTER_THYMELEAF;
  }

  /**
   * Попытка регистрации нового пользователя.
   *
   * @param registerUserDto Введенные данные пользователя.
   * @param result          Результат первичной проверки.
   * @param model           моделью
   * @return окно.
   */
  @PostMapping(REGISTER_THYMELEAF)
  public String registerUser(@Valid RegisterUserDto registerUserDto, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return REGISTER_THYMELEAF;
    }
    String phone = registerUserDto.getPhone();
    if (!phone.matches(CANONICAL_PHONE_PATTERN)) {
      registerUserDto.setPhone(phoneToCanonical(phone));
    }

    log.info("Получен запрос на создание пользователя {}", registerUserDto);
    RegisterResult mayRegister = registerServices.isMayRegister(registerUserDto);
    try {
      return switch (mayRegister.getType()) {
        case REGISTERED_SUCCESS -> {
          registerServices.createUser(registerUserDto, false);
          model.addAttribute("nick", registerUserDto.getNick());
          model.addAttribute("NeedAccept", false);
          yield SET_PASSWORD_THYMELEAF;
        }
        case PRE_REGISTERED -> {
          registerServices.createUser(registerUserDto, true);
          model.addAttribute("nick", registerUserDto.getNick());
          model.addAttribute("NeedAccept", true);
          yield SET_PASSWORD_THYMELEAF;
        }
        case USER_EXISTS -> {
          model.addAttribute("errorHead", "Ошибка при регистрации");
          model.addAttribute("errorText", mayRegister.getAddInfo());
          yield WRONG_THYMELEAF;
        }
        case DUPLICATE_FIELD -> {
          String addInfo = mayRegister.getAddInfo();
          if (addInfo.contains(NICK_FIELD)) {
            model.addAttribute("nickError", EXISTS_IN_DB.formatted("пользователь"));
          }
          if (addInfo.contains(PHONE_FIELD)) {
            model.addAttribute("phoneError", EXISTS_IN_DB.formatted("телефон"));
          }
          if (addInfo.contains(EMAIL_FIELD)) {
            model.addAttribute("emailError", EXISTS_IN_DB.formatted("адрес"));
          }
          yield REGISTER_THYMELEAF;
        }
      };
    } catch (Exception ex) {
      log.error("Ошибка при создании пользователя {}: {}", registerUserDto.getNick(), ex.getMessage(), ex);
      model.addAttribute("errorHead", "Ошибка при создании пользователя.");
      model.addAttribute("errorText", ex.getMessage());
      return WRONG_THYMELEAF;
    }
  }

  /**
   * Ввод пароля.
   *
   * @param nick пользователь.
   * @param newPassword пароль.
   * @param reEnteredPassword его копия.
   * @param model модель.
   * @return thymeleaf html.
   */
  @PostMapping(SET_PASSWORD_THYMELEAF)
  public String setNewPassword(@RequestParam String nick, @RequestParam String newPassword,
                               @RequestParam String reEnteredPassword, Model model) {
    String passwordRemarks = registerServices.checkPassword(newPassword, reEnteredPassword);
    if (passwordRemarks.isEmpty()) {
      try {
        registerServices.createPassword(nick, newPassword);
        return "login";
      } catch (Exception ex) {
        log.error("Ошибка при задания пароля у пользователя {}: {}", nick, ex.getMessage(), ex);
        model.addAttribute("errorHead", "Ошибка при задания пароля.");
        model.addAttribute("errorText", ex.getMessage());
        return WRONG_THYMELEAF;
      }
    }
    model.addAttribute("nick", nick);
    model.addAttribute("passwordRemarks", passwordRemarks);
    return SET_PASSWORD_THYMELEAF;
  }
}
