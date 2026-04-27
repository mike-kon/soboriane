package com.mikesoft.soboriane.dao;

import com.mikesoft.soboriane.dto.web.RegisterUserDto;
import com.mikesoft.soboriane.dto.web.UserLoginDto;
import com.mikesoft.soboriane.util.UuidV7;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

/**
 * Взаимодействие с БД в части работы с пользователем.
 */
@Component
@RequiredArgsConstructor
public class UserDao {

  private static final String USER_SHADOW =
      """
          select u.nick, u."name", u."family", u.birthday, u.angel_days, u."number", u.is_admin,
                 u.day_begin, u.day_end, s."password", s.enabled, s.created as passwd_creator,
                 s.expires as passwd_expired
          from app_user u
                 left join shadow s on u.nick  = s."user"
          where u.nick = :user
          """;

  private static final String USER_PHONE_SHADOW =
      """
          select u."nick", u."name", u."family", u.birthday, u.angel_days, u."number", u.is_admin,
                 u.day_begin, u.day_end, u.phone, u.email, s."password",
                 s.enabled, s.created as passwd_creator, s.expires as passwd_expired
          from app_user u left join shadow s on u.nick  = s."user"
          where u.nick = :nick or phone = :phone or email = :email
          """;

  private static final String INSERT_NEW_USER =
      """
          insert into app_user
            (nick, "name", "family", birthday, angel_days, "number", is_admin,
                 day_begin, day_end, phone, email)
          VALUES(:nick, :name, :family, :birthday, :angelDays, :number, false,
                 :dayBegin, null, :phone, :mail)
          """;

  private static final String IS_EXIST_SHADOW =
      """
          select 1 from shadow where "user" = :user
          """;

  private static final String UPDATE_PASSWORD =
      """
          update shadow set "password" = :password,
                            created = :created,
                            expires = :expires,
                            enable = :enabled
          where "user" = :user
          """;

  private static final String INSERT_PASSWORD =
      """
          insert into shadow (id, "user", "password", enabled, created, expires)
                values (:id, :user, :password, :enabled, :created, :expires)
          """;

  private static final String DELETE_USER =
      """
          delete from app_user where nick = :nick
          """;

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  /**
   * Информация о пользователе и его параметрах авторизации.
   *
   * @param user пользователь.
   * @return структура UserLoginDto.
   */
  public Optional<UserLoginDto> getUser(String user) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("user", user);
    List<UserLoginDto> res = namedParameterJdbcTemplate.query(USER_SHADOW, params,
        new DataClassRowMapper<>(UserLoginDto.class));
    return res.stream().findFirst();
  }

  /**
   * Поиск по пользователю и телефону.
   *
   * @param nick  пользователь
   * @param phone телефон
   * @return список найденных пользователей.
   */
  public List<UserLoginDto> getUser(String nick, String phone, String email) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("nick", nick)
        .addValue("phone", phone)
        .addValue("email", email);
    return namedParameterJdbcTemplate.query(USER_PHONE_SHADOW, params,
        new DataClassRowMapper<>(UserLoginDto.class));
  }

  /**
   * Создание нового пользователя.
   *
   * @param registerUserDto информация о пользователе.
   */
  public void createUser(@Valid RegisterUserDto registerUserDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("nick", registerUserDto.getNick())
        .addValue("name", registerUserDto.getName())
        .addValue("family", registerUserDto.getFamily())
        .addValue("birthday", registerUserDto.getBirthday())
        .addValue("angelDays", registerUserDto.getAngelDays())
        .addValue("number", registerUserDto.getNumber())
        .addValue("dayBegin", LocalDate.now())
        .addValue("phone", registerUserDto.getPhone())
        .addValue("mail", registerUserDto.getEmail());

    namedParameterJdbcTemplate.update(INSERT_NEW_USER, params);
  }

  public void deleteUser(String nick) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("nick", nick);
    namedParameterJdbcTemplate.update(DELETE_USER, params);
  }

  public void updatePassword(String nick, String encodePassword, Boolean needAccept,
                             LocalDateTime created, LocalDateTime expiries) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", UuidV7.generate())
        .addValue("user", nick)
        .addValue("password", encodePassword)
        .addValue("created", created)
        .addValue("expires", expiries)
        .addValue("enabled", !needAccept);

    List<Object> shadow = namedParameterJdbcTemplate.query(IS_EXIST_SHADOW, params,
        new DataClassRowMapper<>());
    String actualSql = shadow.isEmpty() ? INSERT_PASSWORD : UPDATE_PASSWORD;
    namedParameterJdbcTemplate.update(actualSql, params);
  }

}
