package com.gucardev.dynamicscheduledtasks.controller;

import com.gucardev.dynamicscheduledtasks.model.TaskDetail;
import com.gucardev.dynamicscheduledtasks.service.TaskService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {
  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<List<TaskDetail>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @PostMapping
  public ResponseEntity<TaskDetail> scheduleTask(@RequestBody TaskDetail taskDetail) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.scheduleTask(taskDetail));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDetail> updateTask(
      @PathVariable UUID id, @RequestBody TaskDetail taskDetail) {
    TaskDetail updatedTaskDetail = taskService.updateTask(id, taskDetail);
    if (updatedTaskDetail == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(updatedTaskDetail);
  }
}
