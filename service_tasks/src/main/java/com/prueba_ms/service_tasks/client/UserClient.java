package com.prueba_ms.service_tasks.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.prueba_ms.service_tasks.dto.UserDTO;

@FeignClient(name = "service-users") 
public interface UserClient {

    // El endpoint que ya creamos en el otro MS
    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
