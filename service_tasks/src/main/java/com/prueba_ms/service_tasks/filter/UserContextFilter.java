package com.prueba_ms.service_tasks.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Order(1)
public class UserContextFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // OBTENER TODOS LOS HEADERS PARA DEBUG
        System.out.println("=== USER CONTEXT FILTER START ===");
        System.out.println("Request URI: " + httpRequest.getRequestURI());
        
        // Obtener headers específicos
        String userRole = httpRequest.getHeader("X-User-Role");
        String userDocument = httpRequest.getHeader("X-User-Document");
        
        System.out.println("Header 'X-User-Role': " + userRole);
        System.out.println("Header 'X-User-Document': " + userDocument);
        
        // LISTAR TODOS LOS HEADERS para debug
        System.out.println("Todos los headers recibidos:");
        java.util.Collections.list(httpRequest.getHeaderNames())
            .forEach(headerName -> {
                System.out.println("  " + headerName + ": " + httpRequest.getHeader(headerName));
            });
        
        if (userRole != null && !userRole.isEmpty()) {
            // Guardar en RequestContextHolder
            RequestContextHolder.currentRequestAttributes()
                .setAttribute("role", userRole, 
                    org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);
            RequestContextHolder.currentRequestAttributes()
                .setAttribute("document", userDocument != null ? userDocument : "",
                    org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);
            
            System.out.println("✅ Rol guardado en contexto: " + userRole);
        } else {
            System.out.println("⚠️ No se encontró el header X-User-Role");
        }
        
        System.out.println("=== USER CONTEXT FILTER END ===");
        chain.doFilter(request, response);
    }
}