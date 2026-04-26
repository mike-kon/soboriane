package com.mikesoft.soboriane.config;

import com.mikesoft.soboriane.security.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасного соединения.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserLoginService loginService;

  /**
   * Кодировщик парольной строки.
   *
   * @return Тип кодировщика.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Настройка страниц для доступа с авторизацией и без.
   *
   * @param http протокол.
   * @return фильтр.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/login", "/register").permitAll() // Открытые страницы
            .anyRequest().authenticated()) // Все остальные требуют аутентификации
        .formLogin(form -> form
            .loginPage("/login") // Кастомная страница входа
            .defaultSuccessUrl("/", true)
            .usernameParameter("user")
            .passwordParameter("password")
            .permitAll()
        )
        .logout(LogoutConfigurer::permitAll)
        .userDetailsService(loginService);
    return http.build();
  }

}
