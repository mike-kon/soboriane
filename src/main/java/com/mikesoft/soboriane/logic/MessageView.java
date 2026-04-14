package com.mikesoft.soboriane.logic;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("btnMessage")
public class MessageView implements MenuView {

  @Override
  public String loadView(String sessionId, String nick, Model model) {
    return "messages";
  }

}
