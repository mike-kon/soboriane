package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.db.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

  @GetMapping("/")
  @Deprecated
  public String index(HttpServletRequest request, @AuthenticationPrincipal UserLoginDto user, Model model) {
    model.addAttribute("sessionId", request.getSession().getId());
    model.addAttribute("user", user.getNick());
    return "index-old";
  }

  @GetMapping("login")
  public String login() {
    return "login";
  }

}
