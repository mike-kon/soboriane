package com.mikesoft.soboriane.dto.db;

import com.mikesoft.soboriane.enums.MessageType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сообщение.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
  private UUID id;
  private LocalDateTime created;
  private UserDto owner;
  private MessageType type;
  private String message;
  private List<String> prayer;
}
