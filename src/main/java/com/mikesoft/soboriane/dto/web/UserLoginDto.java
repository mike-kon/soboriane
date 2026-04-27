package com.mikesoft.soboriane.dto.web;

import com.mikesoft.soboriane.dto.db.UserDto;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Данные авторизации пользователя.
 */
@Getter
@Setter
public class UserLoginDto extends UserDto implements UserDetails {

  private String password;
  private Boolean enabled;
  private LocalDateTime passwdCreator;
  private LocalDateTime passwdExpired;

  @Override
  @NullMarked
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  @NullMarked
  public String getUsername() {
    return getNick();
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return password != null;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    LocalDateTime now = LocalDateTime.now();
    return now.isAfter(passwdCreator) && passwdExpired == null || now.isBefore(passwdExpired);
  }
}
