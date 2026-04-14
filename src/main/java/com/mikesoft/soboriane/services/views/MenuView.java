package com.mikesoft.soboriane.services.views;

import com.mikesoft.soboriane.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface MenuView {

  String loadView(UserDto nick, Model model);

}
