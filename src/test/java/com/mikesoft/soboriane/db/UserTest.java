package com.mikesoft.soboriane.db;

import com.mikesoft.soboriane.base.PostgresBaseTest;
import com.mikesoft.soboriane.dto.db.User;
import com.mikesoft.soboriane.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest extends PostgresBaseTest {

  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("Проверка корректности настройки тестовой среды")
  void pingTest() {
    assertTrue(true, "Ping");
  }

  @Test
  @DisplayName("Проверка ввода пользователя")
  void createUserTest() {
    User user = new User("user", "UserName", "UserFamily",
      LocalDate.of(1968, 8, 15), LocalDate.of(0, 9, 19),
      1, false, LocalDate.now(), null);

    Optional<User> findBefore = userRepository.findById(user.getNick());
    assertTrue(findBefore.isEmpty());

    assertDoesNotThrow(() -> userRepository.save(user));
    Optional<User> findAfter = userRepository.findById(user.getNick());
    assertTrue(findAfter.isPresent());
    User userDb = findAfter.get();
    assertEquals(user.getName(), userDb.getName());
    assertEquals(user.getFamily(), userDb.getFamily());
    assertEquals(user.getBirthday(), userDb.getBirthday());
    assertEquals(user.getAngelDays(), userDb.getAngelDays());
    assertEquals(user.getNumber(), userDb.getNumber());
    assertEquals(LocalDate.now(), userDb.getDayBegin());
    assertNull(userDb.getDayEnd());
  }

  @Test
  @DisplayName("Проверка контроля уникальности пользователя")
  void checkUniqUserTest() {
    User user1 = new User("user", "UserName1", "UserFamily1",
      LocalDate.of(1968, 8, 15), LocalDate.of(0, 9, 19),
      1, false, LocalDate.now(), null);
    User user2 = new User("user", "UserName2", "UserFamily2",
      LocalDate.of(1968, 8, 15), LocalDate.of(0, 9, 19),
      1, false, LocalDate.now(), null);
    assertDoesNotThrow(() -> userRepository.save(user1));
    // todo определить тип исключения
    assertDoesNotThrow(() -> userRepository.save(user2));
  }

  // Проверка контроля номера

}
