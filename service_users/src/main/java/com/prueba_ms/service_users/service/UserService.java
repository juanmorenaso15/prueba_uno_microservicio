package com.prueba_ms.service_users.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba_ms.service_users.dto.JwtResponseDTO;
import com.prueba_ms.service_users.entity.Employees;
import com.prueba_ms.service_users.repository.EmployeesRepository;

@Service
public class UserService {

    @Autowired
    private EmployeesRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CAMBIO AQUÍ: Ahora inyectamos JwtService en lugar de JwtUtils
    @Autowired
    private JwtService jwtService;

    public List<Employees> getAllUsers() {
        return userRepository.findAll();
    }

    public Employees saveUser(Employees user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Employees getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID proporcionado no puede ser nulo");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // ADAPTACIÓN DEL LOGIN: Usamos JwtService y devolvemos tu DTO oficial
    public JwtResponseDTO login(String documentNumber, String password) {
        // 1. Buscamos el empleado usando el repositorio
        Employees employee = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas (Usuario no encontrado)"));

        // 2. Validamos si la contraseña coincide
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas (Contraseña inválida)");
        }
        
        // 3. Validamos si está activo
        if (!employee.getActive()) {
            throw new RuntimeException("El empleado no se encuentra activo");
        }

        // 4. Generamos el token usando el método oficial de tu JwtService
        String token = jwtService.generateToken(
                employee.getId(), 
                employee.getRole().name(), 
                employee.getDocumentNumber()
        );

        // 5. Retornamos la respuesta estructurada tal como la necesitas
        return new JwtResponseDTO(token, employee.getRole().name(), employee.getName());
    }
}