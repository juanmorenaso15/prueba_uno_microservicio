package com.prueba_ms.service_tasks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba_ms.service_tasks.client.UserClient;
import com.prueba_ms.service_tasks.entity.TaskEntity;
import com.prueba_ms.service_tasks.repository.TaskRepository;

@Service
public class TaskService {
@Autowired
    private TaskRepository taskRepository; // Corregido el nombre del tipo
    
    @Autowired
    private UserClient userClient;

    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public TaskEntity createTask(TaskEntity task) {
        try {
            // Verificamos si el usuario existe llamando al otro MS
            userClient.getUserById(task.getUserId());
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException("Error: El usuario con ID " + task.getUserId() + " no existe en el sistema.");
        }
    }
}