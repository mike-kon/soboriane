package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.UserLoginDto;
import com.mikesoft.soboriane.services.views.MenuView;
import com.mikesoft.soboriane.services.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LogicController {

  private final MessageService messageService;
  private final Map<String, MenuView> menuCommands;

  @PostMapping("menu")
  public String menu(@RequestParam String command, @AuthenticationPrincipal UserLoginDto user, Model model) {
    MenuView view = menuCommands.get(command);
    return view != null ? view.loadView(user, model) : "uc";
  }

  @PostMapping("message")
  @ResponseStatus(HttpStatus.OK)
  public void sendMessage(@RequestParam String message, @AuthenticationPrincipal UserLoginDto user) {
    messageService.messageUpdate(user, message);
  }
}
