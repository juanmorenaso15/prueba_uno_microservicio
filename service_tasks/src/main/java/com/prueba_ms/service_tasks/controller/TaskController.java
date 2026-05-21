package com.prueba_ms.service_tasks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_ms.service_tasks.entity.TaskEntity;
import com.prueba_ms.service_tasks.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController { // Puedes llamarlo TaskController

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TaskEntity> list() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public TaskEntity create(@RequestBody TaskEntity task) {
        return taskService.createTask(task);
    }
}
