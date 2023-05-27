package com.gucardev.dynamicscheduledtasks;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetail {
  private String content;
  private long time;
  private TimeType timeType;
  private LocalDateTime exactTime;
  private LocalDateTime createdAt;
}
