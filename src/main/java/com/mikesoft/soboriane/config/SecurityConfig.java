package com.mikesoft.soboriane.config;

import com.mikesoft.soboriane.security.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserLoginService loginService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/login", "/register").permitAll() // Открытые страницы
            .anyRequest().authenticated() // Все остальные требуют аутентификации
        )
        .formLogin(form -> form
            .loginPage("/login") // Ваша кастомная страница входа
            .defaultSuccessUrl("/", true)
            .usernameParameter("user")
            .passwordParameter("password")
            .permitAll()
        )
        .logout(LogoutConfigurer::permitAll)
        .userDetailsService(loginService); // <-- ВОТ ОНО! Подключаем наш сервис
    return http.build();
  }

}
