package com.mikesoft.soboriane.controllers;

import static com.mikesoft.soboriane.enums.RegisterUnknownUsers.DISABLED;
import static com.mikesoft.soboriane.enums.RegisterUnknownUsers.NEED_ACCEPT;
import static com.mikesoft.soboriane.services.RegisterServices.EMAIL_FIELD;
import static com.mikesoft.soboriane.services.RegisterServices.NICK_FIELD;
import static com.mikesoft.soboriane.services.RegisterServices.PHONE_FIELD;
import static com.mikesoft.soboriane.util.MyWebUtils.addDebugElement;
import static com.mikesoft.soboriane.util.PhoneUtil.CANONICAL_PHONE_PATTERN;
import static com.mikesoft.soboriane.util.PhoneUtil.phoneToCanonical;

import com.mikesoft.soboriane.config.AppWebProperties;
import com.mikesoft.soboriane.dto.web.RegisterUserDto;
import com.mikesoft.soboriane.dto.web.RegisterResult;
import com.mikesoft.soboriane.services.RegisterServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
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

  public static final String REGISTER_THYMELEAF = "register";
  public static final String WRONG_THYMELEAF = "wrong";
  public static final String SET_PASSWORD_THYMELEAF = "setpassword";
  private static final String EXISTS_IN_DB = "Такой %s уже существует в БД";
  private static final String ERROR_HEAD = "errorHead";
  private static final String ERROR_TEXT = "errorText";
  private static final String UNKNOWN_REGISTER_DEPRECATED =
      """
          Извините, но пока регистрация неизвестных пользователей не реализована.
          Свяжитесь с автором.
          """;
  private static final String REGISTER_OVERDUE =
      """
          Вы слишком долго придумывали пароль (больше3-х дней)
          Начните регистрацию заново.
          """;
  public static final String NICK_MODEL_ATTRIBUTE = "nick";
  public static final String SESSION_ID_MODEL_ATTRIBUTE = "sessionId";
  public static final String NEED_ACCEPT_MODEL_ATTRIBUTE = "needAccept";
  public static final String REGISTER_USER_DTO_MODEL_ATTRIBUTE = "registerUserDto";
  public static final String NICK_ERROR_MODEL_ATTRIBUTE = "nickError";
  public static final String PHONE_ERROR_MODEL_ATTRIBUTE = "phoneError";
  public static final String EMAIL_ERROR_MODEL_ATTRIBUTE = "emailError";
  public static final String PASSWORD_REMARKS_MODEL_ATTRIBUTE = "passwordRemarks";

  private final RegisterServices registerServices;
  private final AppWebProperties appWebProperties;

  /**
   * Авторизация.
   *
   * @return - thymeleaf модель.thymeleaf модель.
   */
  @GetMapping(REGISTER_THYMELEAF)
  public String register(HttpSession session, Model model) {
    addDebugElement(appWebProperties.getShowDebugElement(), model, session.getId());
    model.addAttribute(REGISTER_USER_DTO_MODEL_ATTRIBUTE, new RegisterUserDto());
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
  public String registerUser(HttpSession session, @Valid RegisterUserDto registerUserDto, BindingResult result, Model model) {
    addDebugElement(appWebProperties.getShowDebugElement(), model, session.getId());
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
          model.addAttribute(NICK_MODEL_ATTRIBUTE, registerUserDto.getNick());
          model.addAttribute(SESSION_ID_MODEL_ATTRIBUTE, session.getId());
          model.addAttribute(NEED_ACCEPT_MODEL_ATTRIBUTE, false);
          yield SET_PASSWORD_THYMELEAF;
        }
        case PRE_REGISTERED -> {
          if (appWebProperties.getRegisterUnknownUsers() == DISABLED) {
            model.addAttribute(ERROR_HEAD, "Регистрация пока невозможна.");
            model.addAttribute(ERROR_TEXT, UNKNOWN_REGISTER_DEPRECATED);
            yield WRONG_THYMELEAF;
          } else {
            boolean needAccept = appWebProperties.getRegisterUnknownUsers() == NEED_ACCEPT;
            registerServices.createUser(registerUserDto, needAccept);
            model.addAttribute(NICK_MODEL_ATTRIBUTE, registerUserDto.getNick());
            model.addAttribute(SESSION_ID_MODEL_ATTRIBUTE, session.getId());
            model.addAttribute(NEED_ACCEPT_MODEL_ATTRIBUTE, needAccept);
            yield SET_PASSWORD_THYMELEAF;
          }
        }
        case USER_EXISTS -> {
          model.addAttribute(ERROR_HEAD, "Ошибка при регистрации");
          model.addAttribute(ERROR_TEXT, mayRegister.getAddInfo());
          yield WRONG_THYMELEAF;
        }
        case DUPLICATE_FIELD -> {
          String addInfo = mayRegister.getAddInfo();
          if (addInfo.contains(NICK_FIELD)) {
            model.addAttribute(NICK_ERROR_MODEL_ATTRIBUTE, EXISTS_IN_DB.formatted("пользователь"));
          }
          if (addInfo.contains(PHONE_FIELD)) {
            model.addAttribute(PHONE_ERROR_MODEL_ATTRIBUTE, EXISTS_IN_DB.formatted("телефон"));
          }
          if (addInfo.contains(EMAIL_FIELD)) {
            model.addAttribute(EMAIL_ERROR_MODEL_ATTRIBUTE, EXISTS_IN_DB.formatted("адрес"));
          }
          yield REGISTER_THYMELEAF;
        }
        case REGISTRATION_OVERDUE -> {
          model.addAttribute(ERROR_HEAD, "Регистрация неуспешна.");
          model.addAttribute(ERROR_TEXT, REGISTER_OVERDUE);
          yield WRONG_THYMELEAF;
        }
        case CONTINUE_WITH_PASSWORD -> {
          model.addAttribute(NICK_MODEL_ATTRIBUTE, registerUserDto.getNick());
          model.addAttribute(SESSION_ID_MODEL_ATTRIBUTE, session.getId());
          model.addAttribute(NEED_ACCEPT_MODEL_ATTRIBUTE, false);
          // TODO: определиться с needAccept
          yield SET_PASSWORD_THYMELEAF;
        }
      };
    } catch (Exception ex) {
      log.error("Ошибка при создании пользователя {}: {}", registerUserDto.getNick(), ex.getMessage(), ex);
      model.addAttribute(ERROR_HEAD, "Ошибка при создании пользователя.");
      model.addAttribute(ERROR_TEXT, ex.getMessage());
      return WRONG_THYMELEAF;
    }
  }

  /**
   * Ввод пароля.
   *
   * @param nick              пользователь.
   * @param newPassword       пароль.
   * @param reEnteredPassword его копия.
   * @param model             модель.
   * @return thymeleaf html.
   */
  @PostMapping(SET_PASSWORD_THYMELEAF)
  public String setNewPassword(HttpSession session, @RequestParam String nick, @RequestParam String sessionId,
                               @RequestParam String newPassword, @RequestParam String reEnteredPassword,
                               @RequestParam Boolean needAccept, Model model) {
    if (nick == null || sessionId == null || !sessionId.equals(session.getId())) {
      return SET_PASSWORD_THYMELEAF;
    }
    addDebugElement(appWebProperties.getShowDebugElement(), model, session.getId());
    List<String> passwordRemarks = registerServices.checkPassword(nick, newPassword, reEnteredPassword);
    if (passwordRemarks.isEmpty()) {
      try {
        registerServices.createPassword(nick, newPassword, needAccept);
        return "login";
      } catch (Exception ex) {
        log.error("Ошибка при задания пароля у пользователя {}: {}", nick, ex.getMessage(), ex);
        model.addAttribute(ERROR_HEAD, "Ошибка при задания пароля.");
        model.addAttribute(ERROR_TEXT, ex.getMessage());
        return WRONG_THYMELEAF;
      }
    }
    model.addAttribute(NICK_MODEL_ATTRIBUTE, nick);
    model.addAttribute(SESSION_ID_MODEL_ATTRIBUTE, session.getId());
    model.addAttribute(NEED_ACCEPT_MODEL_ATTRIBUTE, needAccept);
    model.addAttribute(PASSWORD_REMARKS_MODEL_ATTRIBUTE, passwordRemarks);
    return SET_PASSWORD_THYMELEAF;
  }

}

