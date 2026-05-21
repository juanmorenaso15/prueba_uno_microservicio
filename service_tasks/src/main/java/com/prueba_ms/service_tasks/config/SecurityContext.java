package com.prueba_ms.service_tasks.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class SecurityContext {

    public String getCurrentRole() {
        try {
            // Intentar obtener del RequestContextHolder primero
            Object role = null;
            if (RequestContextHolder.getRequestAttributes() != null) {
                role = RequestContextHolder.getRequestAttributes()
                        .getAttribute("role", RequestAttributes.SCOPE_REQUEST);
            }
            
            // Si no está en el contexto, obtener directamente del header HTTP
            if (role == null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                role = request.getHeader("X-User-Role");
                System.out.println("🔍 SecurityContext - Leyendo rol directamente del header: " + role);
            } else {
                System.out.println("🔍 SecurityContext - Leyendo rol del contexto: " + role);
            }
            
            return role != null ? role.toString() : null;
        } catch (Exception e) {
            System.out.println("❌ Error en SecurityContext: " + e.getMessage());
            return null;
        }
    }
    
    public String getCurrentDocument() {
        try {
            Object document = null;
            if (RequestContextHolder.getRequestAttributes() != null) {
                document = RequestContextHolder.getRequestAttributes()
                        .getAttribute("document", RequestAttributes.SCOPE_REQUEST);
            }
            
            if (document == null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                document = request.getHeader("X-User-Document");
            }
            
            return document != null ? document.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}