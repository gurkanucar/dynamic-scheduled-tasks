# Create Scheduled Tasks Dynamically using Spring Boot

#### It uses ```ScheduledExecutorService``` for create scheduled tasks and ```CompletableFuture``` for execute tasks.

- #### example requests:

```json

{
  "content": "task seconds later",
  "time": 5,
  "timeType": "SECONDS"
}


{
  "content": "task minutes later",
  "time": 1,
  "timeType": "MINUTES"
}

{
  "content": "exact task",
  "timeType": "EXACT_TIME",
  "exactTime": "2023-05-28T01:45:00"
}

```

- #### example logs when task executed

```shell
# 5 seconds later
2023-05-28 01:32:26.878  INFO 14524 --- [nPool-worker-19] c.g.dynamicscheduledtasks.TaskService    : task processed: created at 2023-05-28T01:32:21.870490800 | content: task seconds later

# 1 minutes later
2023-05-28 01:33:43.375  INFO 14524 --- [nPool-worker-19] c.g.dynamicscheduledtasks.TaskService    : task processed: created at 2023-05-28T01:32:43.358745 | content: task minutes later

# scheduled at "2023-05-28T01:45:00"
2023-05-28 01:44:59.584  INFO 17160 --- [nPool-worker-19] c.g.dynamicscheduledtasks.TaskService    : task processed: created at 2023-05-28T01:43:28.579356300 | content: exact task
```

- #### postman collection

[https://github.com/gurkanucar/dynamic-scheduled-tasks/blob/master/dynamic%20task%20scheduling.postman_collection.json](https://github.com/gurkanucar/dynamic-scheduled-tasks/blob/master/dynamic%20task%20scheduling.postman_collection.json)