package com.prueba_ms.service_users.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba_ms.service_users.entity.UserEntity;
import com.prueba_ms.service_users.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

public UserEntity saveUser(UserEntity user) {
        // Validación preventiva
        Objects.requireNonNull(user, "El objeto usuario no puede ser nulo");
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long id) {
        // Validamos que el ID no sea nulo antes de buscar
        if (id == null) {
            throw new IllegalArgumentException("El ID proporcionado no puede ser nulo");
        }
        
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}