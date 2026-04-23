package com.mikesoft.soboriane.services.views;

import com.mikesoft.soboriane.dto.db.UserDto;
import org.springframework.ui.Model;

/**
 * Интерфейс вьюера.
 */
public interface MenuView {

  /**
   * Загрузка вьюера.
   *
   * @param nick прользователь.
   * @param model  модель для формирования thymeleaf модели.
   * @return thymeleaf модель.
   */
  String loadView(UserDto nick, Model model);

}
