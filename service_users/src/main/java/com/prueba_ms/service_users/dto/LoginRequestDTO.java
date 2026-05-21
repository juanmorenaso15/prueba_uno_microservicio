package com.prueba_ms.service_users.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String documentNumber;
    private String password;
}