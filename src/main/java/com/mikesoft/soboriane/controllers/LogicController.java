package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.db.UserLoginDto;
import com.mikesoft.soboriane.logic.MenuView;
import com.mikesoft.soboriane.logic.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogicController {

  private final MessageService messageService;
  private final Map<String, MenuView> menuCommands;

  @PostMapping("menu")
  public String menu(@RequestParam String command, HttpServletRequest request,
                     @AuthenticationPrincipal UserLoginDto user, Model model) {
    MenuView view = menuCommands.get(command);
    String sessionId = request.getSession().getId();
    String nick = user.getNick();
    return view != null ? view.loadView(sessionId, nick, model) : "uc";
  }

  @PostMapping("message")
  @ResponseStatus(HttpStatus.OK)
  public void sendMessage(@RequestParam String message, @AuthenticationPrincipal UserLoginDto user) {
    messageService.messageUpdate(user.getNick(), message);
  }
}
