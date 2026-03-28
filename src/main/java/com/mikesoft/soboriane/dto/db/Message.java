package com.mikesoft.soboriane.dto.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "message", schema = "soboriane")
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @ColumnDefault("now()")
  @Column(name = "created", nullable = false)
  private Instant created;

  @Column(name = "owner", nullable = false, length = Integer.MAX_VALUE)
  private String owner;

  @Column(name = "type", nullable = false, length = Integer.MAX_VALUE)
  private String type;

  @Column(name = "message", nullable = false, length = Integer.MAX_VALUE)
  private String messageText;

  @Column(name = "prayer")
  private List<String> prayer;

}