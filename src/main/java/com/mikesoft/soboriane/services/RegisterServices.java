package com.mikesoft.soboriane.services;

import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.CONTINUE_WITH_PASSWORD;
import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.DUPLICATE_FIELD;
import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.PRE_REGISTERED;
import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.REGISTERED_SUCCESS;
import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.REGISTRATION_OVERDUE;
import static com.mikesoft.soboriane.dto.web.RegisterResult.RegisterResultType.USER_EXISTS;

import com.mikesoft.soboriane.config.UserProperties;
import com.mikesoft.soboriane.dao.UserDao;
import com.mikesoft.soboriane.dto.web.RegisterUserDto;
import com.mikesoft.soboriane.dto.web.UserLoginDto;
import com.mikesoft.soboriane.dto.web.RegisterResult;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private static final String[] WRONG_PASSWORD = {
      "qwertyui",
      "asdfghjk",
      "password",
      "zxcvbnm"
  };
  private static final String PUNCTUATIONS = ",.;'/\\!@##$%$^&*()-=_+";

  private final UserDao userDao;
  private final LoadPreRegister loadPreRegister;
  private final UserProperties userProperties;

  private List<LoadPreRegister.DataItem> preload;

  @PostConstruct
  public void init() {
    preload = loadPreRegister.getPreRegData();
  }

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
      boolean preReg = preload.stream()
          .anyMatch(u -> newUser.getName().equals(u.getName())
              && newUser.getFamily().equals(u.getFamily())
              && newUser.getBirthday().equals(u.getBirthday())
              && phone.equals(u.getPhone())
          );
      return preReg ? new RegisterResult(REGISTERED_SUCCESS)
          : new RegisterResult(PRE_REGISTERED);
    }
    if (users.size() == 1) {
      // Такой пользователь уже есть?
      UserLoginDto user = users.getFirst();
      if (nick.equals(user.getNick()) && phone.equals(user.getPhone()) && email.equals(user.getEmail())) {
        // Повторная регистрация.
        if (!user.isAccountNonLocked() ) {
          LocalDate endOfRegistration = user.getDayBegin().plusDays(userProperties.getDayToRegistration());
          if (endOfRegistration.isAfter(LocalDate.now())) {
            return new RegisterResult(CONTINUE_WITH_PASSWORD);
          } else {
            userDao.deleteUser(nick);
            return new RegisterResult(REGISTRATION_OVERDUE);
          }
        } else {
          String info = getUserState(user);
          log.warn("Попытка повторной регистрации. Пользователь: {}. Результат: {}", nick, info);
          return new RegisterResult(USER_EXISTS, info);
        }
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
  }

  /**
   * Метод проверки сложности пароля.
   *
   * @param newPassword пароль.
   * @param reEnteredPassword его повторный ввод.
   * @return результат анализа.
   */
  public List<String> checkPassword(String nick, String newPassword, String reEnteredPassword) {
    if (!newPassword.equals(reEnteredPassword)) {
      return List.of("Пароли различаются.");
    }
    List<String> result = new ArrayList<>();
    UserProperties.PasswordSettings passwordSetting =  userProperties.getPasswordSettings();
    if (passwordSetting.isControl()) {
      String lowPassword = newPassword.toLowerCase();
      if (lowPassword.contains(nick.toLowerCase())) {
        result.add("В пароле присутствует имя пользователя.");
      }
      if (Arrays.stream(WRONG_PASSWORD)
          .anyMatch(lowPassword::contains)) {
        result.add("Очень простой пароль.");
      }
      if (newPassword.length() < passwordSetting.getMinLenPassword()) {
        result.add("Очень короткий пароль.");
      }
      long upCase = newPassword.chars().filter(Character::isUpperCase).count();
      long lowCase = newPassword.chars().filter(Character::isLowerCase).count();
      if (upCase < passwordSetting.getOtherRegisterCount() || lowCase < passwordSetting.getOtherRegisterCount()) {
        result.add("Нужно больше букв в разных регистрах.");
      }
      long digits = newPassword.chars().filter(Character::isDigit).count();
      if (digits < passwordSetting.getMinDigital()) {
        result.add("Нужно больше цифр.");
      }
      long punctuation = newPassword.chars()
          .filter(c -> PUNCTUATIONS.indexOf(c) != -1)
          .count();
      if (punctuation < passwordSetting.getMinPunctuation()) {
        result.add("Нужно больше знаков препинания.");
      }
    }
    return result;
  }

  /**
   * Создать пароль.
   *
   * @param nick Пользователь
   * @param newPassword пароль
   * @param needAccept необходимость акцепта админа.
   */
  public void createPassword(String nick, String newPassword,  Boolean needAccept) {
    try {
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      String encodedPassword = encoder.encode(newPassword);
      LocalDateTime created = LocalDateTime.now();
      Period passwordLive = userProperties.getPasswordSettings().getPasswordLive();
      LocalDateTime expired = LocalDateTime.now().plus(passwordLive);
      userDao.updatePassword(nick, encodedPassword, needAccept, created, expired);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private static @NonNull String getUserState(UserLoginDto user) {
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
    return info;
  }

}
