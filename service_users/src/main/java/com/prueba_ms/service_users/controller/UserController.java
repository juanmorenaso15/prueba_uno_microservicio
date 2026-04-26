package com.prueba_ms.service_users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_ms.service_users.entity.UserEntity;
import com.prueba_ms.service_users.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> list() {
        return userService.getAllUsers();
    }
    
    // Nuevo: Endpoint para obtener un usuario por ID
    @GetMapping("/{id}")
    public UserEntity getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserEntity create(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }
}