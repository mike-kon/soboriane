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

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "\"user\"", schema = "soboriane")
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @Column(name = "nick", nullable = false, length = 32)
  private String nick;

  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  private String name;

  @Column(name = "family", nullable = false, length = Integer.MAX_VALUE)
  private String family;

  @Column(name = "birthday", nullable = false)
  private LocalDate birthday;

  @Column(name = "angel_days", nullable = false)
  private LocalDate angelDays;

  @Column(name = "number", nullable = false)
  private Integer number;

  @ColumnDefault("false")
  @Column(name = "is_admin", nullable = false)
  private Boolean isAdmin;

  @Column(name = "day_begin", nullable = false)
  private LocalDate dayBegin;

  @Column(name = "day_end")
  private LocalDate dayEnd;


}