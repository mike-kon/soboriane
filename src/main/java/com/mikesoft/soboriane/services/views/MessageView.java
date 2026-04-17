package com.mikesoft.soboriane.services.views;

import com.mikesoft.soboriane.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Вьюер сообщений.
 */
@Component("btnMessage")
@RequiredArgsConstructor
public class MessageView implements MenuView {

  @Override
  public String loadView(UserDto nick, Model model) {
    return "messages";
  }
}
