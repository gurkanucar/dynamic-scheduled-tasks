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
      "exactTime": "2023-05-28T01:16:00"
    }

```

- #### postman collection

[https://github.com/gurkanucar/dynamic-scheduled-tasks/blob/master/dynamic%20task%20scheduling.postman_collection.json](https://github.com/gurkanucar/dynamic-scheduled-tasks/blob/master/dynamic%20task%20scheduling.postman_collection.json)