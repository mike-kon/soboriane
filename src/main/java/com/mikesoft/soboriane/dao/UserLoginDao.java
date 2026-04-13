package com.mikesoft.soboriane.dao;

import com.mikesoft.soboriane.dto.db.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserLoginDao {

  private final String USER_SHADOW_ALL = """
      select u.nick, u."name", u."family", s."password", s.enabled  from app_user u 
      inner join shadow s on u.nick  = s."user"      
      """;

  private final String USER_SHADOW = """
      select u.nick, u."name", u."family", s."password", s.enabled  from app_user u 
      inner join shadow s on u.nick  = s."user"
      where u.nick = :user
      """;

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<UserLoginDto> getAllUsers() {
    SqlParameterSource params = new MapSqlParameterSource();
    return jdbcTemplate.queryForList(USER_SHADOW_ALL, params, UserLoginDto.class);
  }

  public UserLoginDto getUser(String user) {
    // todo Добавить обработку смены пароля
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("user", user);
    return jdbcTemplate.queryForObject(USER_SHADOW, params, new DataClassRowMapper<>(UserLoginDto.class));
  }
}
