package com.prueba_ms.service_tasks.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String error;
    private String mensaje;
    private int codigo;
    private LocalDateTime fecha;
}
