package com.gucardev.dynamicscheduledtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DynamicScheduledTasksApplication {

  public static void main(String[] args) {
    SpringApplication.run(DynamicScheduledTasksApplication.class, args);
  }

}
