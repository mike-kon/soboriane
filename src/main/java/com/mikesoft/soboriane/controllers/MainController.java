package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.dto.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Slf4j
public class MainController {

  @GetMapping("/")
  @Deprecated
  public String index(@AuthenticationPrincipal UserLoginDto userLogin, Model model) {
    return "index";
  }

  @GetMapping("login")
  public String login() {
    return "login";
  }

}
