package com.prueba_ms.service_users.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_ms.service_users.dto.JwtResponseDTO;
import com.prueba_ms.service_users.dto.LoginRequestDTO;
import com.prueba_ms.service_users.dto.MessageResponseDTO;
import com.prueba_ms.service_users.entity.Employees;
import com.prueba_ms.service_users.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Employees> list() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Employees getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Employees create(@RequestBody Employees user) {
        return userService.saveUser(user);
    }

    // ENDPOINT DE LOGIN NUEVO
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // CAMBIO AQUÍ: Cambia 'String token' por 'JwtResponseDTO response'
            JwtResponseDTO response = userService.login(loginRequest.getDocumentNumber(), loginRequest.getPassword());

            // Retornamos el objeto completo con el estado OK (200)
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO("Error de autenticación: " + e.getMessage()));
        }
    }
}