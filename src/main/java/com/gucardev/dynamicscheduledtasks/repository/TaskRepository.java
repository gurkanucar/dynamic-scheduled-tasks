package com.gucardev.dynamicscheduledtasks.repository;

import com.gucardev.dynamicscheduledtasks.model.TaskDetail;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskDetail, UUID> {

  List<TaskDetail> findAllByExecuted(boolean executed);

}
