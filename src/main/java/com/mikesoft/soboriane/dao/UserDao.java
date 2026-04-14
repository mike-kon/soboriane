package com.mikesoft.soboriane.dao;

import com.mikesoft.soboriane.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDao {

  private static final String USER_SHADOW = """
      select u.nick, u."name", u."family", u.birthday, u.angel_days, u."number", u.is_admin,
             u.day_begin, u.day_end, s."password", s.enabled  from app_user u
                 inner join shadow s on u.nick  = s."user"
             where u.nick = :user
      """;

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public UserLoginDto getUser(String user) {
    // todo Добавить обработку смены пароля (вот только где)
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("user", user);
    return jdbcTemplate.queryForObject(USER_SHADOW, params, new DataClassRowMapper<>(UserLoginDto.class));
  }
}
