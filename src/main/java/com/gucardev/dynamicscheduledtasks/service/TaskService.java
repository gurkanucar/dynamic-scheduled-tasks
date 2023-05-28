package com.gucardev.dynamicscheduledtasks.service;

import com.gucardev.dynamicscheduledtasks.model.TimeType;
import com.gucardev.dynamicscheduledtasks.model.TaskDetail;
import com.gucardev.dynamicscheduledtasks.repository.TaskRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final ScheduledExecutorService executorService;
  private final TaskRepository taskRepository;

  public TaskService(ScheduledExecutorService executorService, TaskRepository taskRepository) {
    this.executorService = executorService;
    this.taskRepository = taskRepository;
  }

  @PostConstruct
  public void init() {
    List<TaskDetail> tasks = taskRepository.findAll();
    tasks.forEach(this::scheduleTask);
  }

  @Async
  public CompletableFuture<Void> executeTask(UUID taskId) {
    return CompletableFuture.runAsync(
        () -> {
          TaskDetail taskDetails = taskRepository.findById(taskId).orElse(null);
          if (taskDetails != null && !taskDetails.isExecuted()) {
            System.out.println(
                "task processed: created at "
                    + taskDetails.getCreatedAt()
                    + " | content: "
                    + taskDetails.getContent());
            taskDetails.setExecuted(true);
            taskRepository.save(taskDetails);
          }
        });
  }

  public void scheduleTask(TaskDetail taskDetails) {
    taskRepository.save(taskDetails);

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
            TaskDetail latestTaskDetails =
                taskRepository.findById(taskDetails.getId()).orElse(null);
            if (latestTaskDetails != null
                && latestTaskDetails.isUpdated()
                && !latestTaskDetails.isExecuted()) {
              scheduleTask(latestTaskDetails);
            } else {
              executeTask(taskDetails.getId()).get();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        delay,
        TimeUnit.SECONDS);
  }

  public void updateTask(TaskDetail updatedTaskDetails) {
    updatedTaskDetails.setUpdated(true);
    taskRepository.save(updatedTaskDetails);
    scheduleTask(updatedTaskDetails);
  }
}
