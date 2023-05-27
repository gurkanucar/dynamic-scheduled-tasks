package com.gucardev.dynamicscheduledtasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@Slf4j
public class TaskService {

  private final ScheduledExecutorService executorService;

  public TaskService(ScheduledExecutorService executorService) {
    this.executorService = executorService;
  }

  @Async
  public CompletableFuture<Void> executeTask(TaskDetail taskDetails) {
    return CompletableFuture.runAsync(
        () -> {
          log.info(
              "task processed: created at {} | content: {}",
              taskDetails.getCreatedAt(),
              taskDetails.getContent());
        });
  }

  public void scheduleTask(TaskDetail taskDetails) {

    taskDetails.setCreatedAt(LocalDateTime.now());

    long delay = taskDetails.getTime();
    if (TimeType.MINUTES.equals(taskDetails.getTimeType())) {
      delay = delay * 60;
    } else if (TimeType.EXACT_TIME.equals(taskDetails.getTimeType())) {
      Duration duration = Duration.between(LocalDateTime.now(), taskDetails.getExactTime());
      delay = duration.isNegative() || duration.isZero() ? 0 : duration.getSeconds();
    }
    executorService.schedule(
        () -> {
          try {
            executeTask(taskDetails).get();
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        delay,
        TimeUnit.SECONDS);
  }
}
