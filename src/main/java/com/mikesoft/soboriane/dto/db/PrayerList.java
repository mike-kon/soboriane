package com.mikesoft.soboriane.dto.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "prayer_list", schema = "soboriane")
public class PrayerList {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  private String name;

  @Column(name = "what", nullable = false, length = Integer.MAX_VALUE)
  private String what;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner")
  private User owner;

  @Column(name = "submitted", nullable = false)
  private LocalDate submitted;

  @Column(name = "terminated")
  private LocalDate terminated;

  @Column(name = "is_main")
  private Boolean isMain;

  @ColumnDefault("'CREATED'")
  @Column(name = "status", nullable = false, length = Integer.MAX_VALUE)
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "message_ref")
  private Message messageRef;


}