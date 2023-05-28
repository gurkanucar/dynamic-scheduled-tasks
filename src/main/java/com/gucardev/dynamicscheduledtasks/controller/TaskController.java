package com.gucardev.dynamicscheduledtasks.controller;

import com.gucardev.dynamicscheduledtasks.service.TaskService;
import com.gucardev.dynamicscheduledtasks.model.TaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/task")
  public void scheduleTask(@RequestBody TaskDetail taskDetail) {
    taskService.scheduleTask(taskDetail);
  }
}
