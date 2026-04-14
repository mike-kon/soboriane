package com.mikesoft.soboriane.services.views;

import com.mikesoft.soboriane.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("btnMessage")
public class MessageView implements MenuView {

  @Override
  public String loadView(UserDto nick, Model model) {
    return "messages";
  }
}
