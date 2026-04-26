package com.mikesoft.soboriane.services;

import static com.mikesoft.soboriane.enums.RegisterResult.RegisterResultType.DUPLICATE_FIELD;
import static com.mikesoft.soboriane.enums.RegisterResult.RegisterResultType.PRE_REGISTERED;
import static com.mikesoft.soboriane.enums.RegisterResult.RegisterResultType.REGISTERED_SUCCESS;
import static com.mikesoft.soboriane.enums.RegisterResult.RegisterResultType.USER_EXISTS;

import com.mikesoft.soboriane.dao.UserDao;
import com.mikesoft.soboriane.dto.web.RegisterUserDto;
import com.mikesoft.soboriane.dto.web.UserLoginDto;
import com.mikesoft.soboriane.enums.RegisterResult;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис регистрации.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServices {

  public static final String NICK_FIELD = "nick";
  public static final String PHONE_FIELD = "phone";
  public static final String EMAIL_FIELD = "email";

  private final UserDao userDao;
  private final LoadPreRegister loadPreRegister;

  /**
   * Регистрация.
   *
   * @param newUser Новый пользователь.
   */
  public RegisterResult isMayRegister(RegisterUserDto newUser) {

    String nick = newUser.getNick();
    String phone = newUser.getPhone();
    String email = newUser.getEmail();

    List<UserLoginDto> users = userDao.getUser(nick, phone, email);
    if (users.isEmpty()) {
      // Такого пользователя нет. Создаем
      return loadPreRegister.getData().stream()
          .anyMatch(u -> newUser.getName().equals(u.getName())
              && newUser.getFamily().equals(u.getFamily())
              && newUser.getBirthday().equals(u.getBirthday())
              && phone.equals(u.getPhone())
          )
          ? new RegisterResult(REGISTERED_SUCCESS)
          : new RegisterResult(PRE_REGISTERED);
    }
    if (users.size() == 1) {
      // Такой пользователь уже есть?
      UserLoginDto user = users.getFirst();
      if (nick.equals(user.getNick()) && phone.equals(user.getPhone()) && email.equals(user.getEmail())) {
        // Повторная регистрация.
        String info;
        if (!user.isAccountNonLocked()) {
          info = "Пользователь должен создать пароль.";
        } else if (!user.isEnabled()) {
          info = "Пользователь был заблокирован.";
        } else if (!user.isCredentialsNonExpired()) {
          info = "У пользователя просроченный пароль.";
        } else {
          info = "Регистрация была произведена ранее.";
        }
        log.warn("Попытка повторной регистрации. Пользователь: {}. Результат: {}", nick, info);
        return new RegisterResult(USER_EXISTS, info);
      }
    }
    // Повторение выбранных элементов.
    log.error("Пользователь с ником {}, телефоном {}  и email {} существует. И это разные пользователи.",
        nick, phone, email);
    StringBuilder warnInfoBuild = new StringBuilder();
    if (users.stream()
        .anyMatch(x -> nick.equals(x.getNick()))) {
      warnInfoBuild.append(NICK_FIELD);
    }
    if (users.stream()
        .anyMatch(x -> phone.equals(x.getPhone()))) {
      warnInfoBuild.append(PHONE_FIELD);
    }
    if (users.stream()
        .anyMatch(x -> email.equals(x.getEmail()))) {
      warnInfoBuild.append(EMAIL_FIELD);
    }

    return new RegisterResult(DUPLICATE_FIELD, warnInfoBuild.toString());
  }

  /**
   * Регистрация пользователя.
   *
   * @param registerUserDto пользователь.
   * @param isNeedAccept    Требуется ли акцепт.
   */
  public void createUser(@Valid RegisterUserDto registerUserDto, boolean isNeedAccept) {
    userDao.createUser(registerUserDto);
    //TODO: добавить информацию о запросе пароля.
  }

  // TODO: реализовать метод.
  public String checkPassword(String newPassword, String reEnteredPassword) {
    return "";
  }

  // TODO: реализовать метод.
  public void createPassword(String nick, String newPassword) {
  }
}
