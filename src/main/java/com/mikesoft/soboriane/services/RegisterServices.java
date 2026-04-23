package com.mikesoft.soboriane.services;

import com.mikesoft.soboriane.dao.RegisterUserDao;
import com.mikesoft.soboriane.dto.RegisterUserDto;
import com.mikesoft.soboriane.dto.db.UserDto;
import com.mikesoft.soboriane.enums.RegisterResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис регистрации.
 */
@Service
@RequiredArgsConstructor
public class RegisterServices {

  private final RegisterUserDao registerUserDao;

  /**
   * Регистрация.
   * @param newUser Новый пользователь.
   */
  public RegisterResult tryRegister(RegisterUserDto newUser) {
    //TODO: проверить, на уникальность nick
    //TODO: проверить на уникальность телефона
    //TODO: Если телефон есть, то это новый член и регистрация автоматическая
    //В противном случае нужно вмешательство админа.
    Optional<UserDto> user = registerUserDao.getUser(newUser.getNick());
    return null;
  }

  public void changePasword(String nick, String newPassword) {

  }
}
