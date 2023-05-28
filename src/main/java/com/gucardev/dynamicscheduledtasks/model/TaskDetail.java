package com.gucardev.dynamicscheduledtasks.model;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskDetail {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Type(type="uuid-char")
  private UUID id;

  @CreationTimestamp private LocalDateTime createdAt;
  private LocalDateTime exactTime;
  private String content;

  @Enumerated(EnumType.STRING)
  private TimeType timeType;

  private long time;
  private boolean isExecuted;
  private boolean isUpdated;
}
