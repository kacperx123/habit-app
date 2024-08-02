package com.habit.app.component;

import com.habit.app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {


    private final TaskService taskService;

    @Autowired
    public TaskScheduler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "0 0 * * * *") // co godzinę
    public void scheduleRepeatingTasks() {
        taskService.checkAndCreateRepeatingTasks();
    }
}
