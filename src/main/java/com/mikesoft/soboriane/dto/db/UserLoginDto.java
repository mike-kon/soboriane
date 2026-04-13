package com.mikesoft.soboriane.dto.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserLoginDto implements UserDetails {
  private String nick;
  private String name;
  private String family;
  private String password;
  private Boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return nick;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
