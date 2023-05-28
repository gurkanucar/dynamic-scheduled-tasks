package com.gucardev.dynamicscheduledtasks.service;

import com.gucardev.dynamicscheduledtasks.model.TaskDetail;
import com.gucardev.dynamicscheduledtasks.model.TimeType;
import com.gucardev.dynamicscheduledtasks.repository.TaskRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {

  private static final String TASK_PROCESSED_LOG = "Task processed: created at {} | content: {}";

  private final ScheduledExecutorService executorService;
  private final TaskRepository taskRepository;

  public TaskService(ScheduledExecutorService executorService, TaskRepository taskRepository) {
    this.executorService = executorService;
    this.taskRepository = taskRepository;
  }

  @PostConstruct
  public void init() {
    log.info("rescheduling non-executed tasks...");
    List<TaskDetail> tasks = taskRepository.findAllByExecuted(false);
    log.info("{} non-executed tasks found.", tasks.size());
    tasks.forEach(this::scheduleTask);
  }

  @Async
  public CompletableFuture<Void> executeTask(UUID taskId) {
    return CompletableFuture.runAsync(
        () -> {
          TaskDetail taskDetails = taskRepository.findById(taskId).orElse(null);
          if (taskDetails != null && !taskDetails.isExecuted()) {
            log.info(TASK_PROCESSED_LOG, taskDetails.getCreatedAt(), taskDetails.getContent());
            taskDetails.setExecuted(true);
            taskRepository.save(taskDetails);
          }
        });
  }

  public TaskDetail scheduleTask(TaskDetail taskDetails) {
    TaskDetail savedTask = taskRepository.save(taskDetails);
    long delay = calculateDelay(taskDetails);
    executorService.schedule(() -> handleScheduledTask(taskDetails), delay, TimeUnit.SECONDS);
    return savedTask;
  }

  private long calculateDelay(TaskDetail taskDetails) {
    long delay = taskDetails.getTime();
    if (TimeType.MINUTES.equals(taskDetails.getTimeType())) {
      delay = delay * 60;
    } else if (TimeType.EXACT_TIME.equals(taskDetails.getTimeType())) {
      Duration duration = Duration.between(LocalDateTime.now(), taskDetails.getExactTime());
      delay = duration.isNegative() || duration.isZero() ? 0 : duration.getSeconds();
    }
    return delay;
  }

  private void handleScheduledTask(TaskDetail taskDetails) {
    try {
      TaskDetail latestTaskDetails = taskRepository.findById(taskDetails.getId()).orElse(null);
      if (latestTaskDetails != null
          && latestTaskDetails.isUpdated()
          && !latestTaskDetails.isExecuted()) {
        scheduleTask(latestTaskDetails);
      } else {
        executeTask(taskDetails.getId()).get();
      }
    } catch (Exception e) {
      log.error("something went wrong", e);
    }
  }

  public TaskDetail updateTask(UUID id, TaskDetail updatedTaskDetails) {
    TaskDetail existingTaskDetail = taskRepository.findById(id).orElse(null);
    if (existingTaskDetail == null) {
      return null;
    }
    updateTaskDetail(existingTaskDetail, updatedTaskDetails);
    scheduleTask(existingTaskDetail);
    return existingTaskDetail;
  }

  private void updateTaskDetail(TaskDetail existingTaskDetail, TaskDetail updatedTaskDetails) {
    existingTaskDetail.setContent(updatedTaskDetails.getContent());
    existingTaskDetail.setTime(updatedTaskDetails.getTime());
    existingTaskDetail.setTimeType(updatedTaskDetails.getTimeType());
    existingTaskDetail.setExactTime(updatedTaskDetails.getExactTime());
    existingTaskDetail.setUpdated(true);
    taskRepository.save(existingTaskDetail);
  }

  public List<TaskDetail> getAllTasks() {
    return taskRepository.findAll();
  }
}
