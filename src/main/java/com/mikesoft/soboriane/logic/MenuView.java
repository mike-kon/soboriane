package com.mikesoft.soboriane.logic;

import org.springframework.ui.Model;

public interface MenuView {

  String loadView(String sessionId, String nick, Model model);

}
