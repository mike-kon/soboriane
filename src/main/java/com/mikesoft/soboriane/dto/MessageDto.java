package com.mikesoft.soboriane.dto;

import com.mikesoft.soboriane.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
