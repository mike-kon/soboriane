package com.mikesoft.soboriane.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class CreatePasswordForAdminTest {

  @Test
  void createPassword() {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodedPassword = encoder.encode("123456");
    System.out.println(encodedPassword); // Скопируйте это в БД
    Assertions.assertNotNull(encodedPassword);
  }
}
