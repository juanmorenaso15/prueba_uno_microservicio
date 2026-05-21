package com.prueba_ms.service_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba_ms.service_users.entity.Employees;


@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    // Método clave para buscar por documento en el login
    Optional<Employees> findByDocumentNumber(String documentNumber);
}