package com.prueba_ms.service_tasks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba_ms.service_tasks.client.UserClient;
import com.prueba_ms.service_tasks.config.SecurityContext;
import com.prueba_ms.service_tasks.entity.TaskEntity;
import com.prueba_ms.service_tasks.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserClient userClient;

    @Autowired
    private SecurityContext security;

    /**
     * Valida si el rol del usuario actual es administrador de forma segura en texto plano.
     * Retorna un booleano si lo necesitas o dispara la excepción directamente.
     */
    private boolean validateAdminRole() {
        String currentRole = security.getCurrentRole();
        
        // Comparamos ignorando mayúsculas/minúsculas por seguridad
        if (currentRole == null || !"administrator".equalsIgnoreCase(currentRole)) {
            // Usamos RuntimeException estándar para no depender de clases externas
            throw new RuntimeException("Acceso denegado. El rol: '" + currentRole + "' no está permitido.");
        }
        return true;
    }   

public List<TaskEntity> getAllTasks() {
    String role = security.getCurrentRole();
    
    // 1. Imprime en la consola el rol exacto que llega para saber qué está leyendo
    System.out.println("--- DEBUG TASK SERVICE --- El rol recibido es: [" + role + "]");
    
    if (role == null) {
        throw new RuntimeException("Acceso denegado: No se encontró ningún rol en la petición.");
    }

    // 2. Limpiamos espacios y quitamos el prefijo 'ROLE_' si Spring Security lo añadió automáticamente
    String cleanRole = role.trim().replace("ROLE_", "");

    // 3. Validación exacta ignorando mayúsculas/minúsculas
    if (!"administrator".equalsIgnoreCase(cleanRole) && !"cashier".equalsIgnoreCase(cleanRole)) {
        throw new RuntimeException("Acceso denegado: El rol '" + cleanRole + "' no tiene permisos para listar tareas.");
    }
    
    return taskRepository.findAll();
}

    public TaskEntity createTask(TaskEntity task) {
        // 1. Validamos primero el rol de administrador usando nuestro método modular
        validateAdminRole();
        
        // 2. Intentamos verificar la existencia del usuario y guardar la tarea
        try {
            // Verificamos si el usuario existe llamando al MS Users mediante Feign Client
            userClient.getUserById(task.getUserId());
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException("Error: El usuario con ID " + task.getUserId() + " no existe en el sistema o el servicio no está disponible.");
        }
    }
}