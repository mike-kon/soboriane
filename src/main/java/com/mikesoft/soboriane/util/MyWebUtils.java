package com.mikesoft.soboriane.util;

import org.springframework.ui.Model;

/**
 * Некоторые утилиты для Web
 */
public class MyWebUtils {

  private MyWebUtils() {}

  public static void addDebugElement(boolean isShowDebugElement, Model model, String sessionId) {
    if (isShowDebugElement) {
      model.addAttribute("debugSessionId", sessionId);
    }
  }


}
