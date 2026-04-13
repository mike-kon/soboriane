package com.mikesoft.soboriane.controllers;

import com.mikesoft.soboriane.config.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/init")
@RequiredArgsConstructor
public class InitController {

  private final ClientProperties clientProperties;

  @PostMapping("clientProperties")
  public ClientProperties getClientProperties() {
    return clientProperties;
  }
}
