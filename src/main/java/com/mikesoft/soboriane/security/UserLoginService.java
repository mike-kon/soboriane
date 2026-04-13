package com.mikesoft.soboriane.security;

import com.mikesoft.soboriane.dao.UserLoginDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {

  private final UserLoginDao userLoginDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userLoginDao.getUser(username);
  }
}
