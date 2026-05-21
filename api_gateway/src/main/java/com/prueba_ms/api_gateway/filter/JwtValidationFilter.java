package com.prueba_ms.api_gateway.filter;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.prueba_ms.api_gateway.config.JwtUtils;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public JwtValidationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Reservado para configuraciones extras de las rutas si se requiere
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            System.out.println("=== GATEWAY FILTER ===");
            System.out.println("Path solicitado: " + path);
            
            // Lista de rutas públicas (no requieren token)
            List<String> publicPaths = Arrays.asList("/api/auth/login", "/api/auth/register");
            
            // Si la ruta es pública, continuar sin validar
            if (publicPaths.stream().anyMatch(path::contains)) {
                System.out.println("Ruta pública, no se valida token");
                return chain.filter(exchange);
            }

            System.out.println("Ruta protegida, validando token...");
            
            // 2. Buscar la cabecera Authorization
            if (!request.getHeaders().containsKey("Authorization")) {
                System.out.println("ERROR: No hay header Authorization");
                return onError(exchange, "Acceso denegado: Falta el token de autenticación", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
            System.out.println("Auth Header: " + authHeader);
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("ERROR: Formato de token inválido");
                return onError(exchange, "Acceso denegado: Formato de token inválido", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            System.out.println("Token extraído: " + token.substring(0, Math.min(token.length(), 20)) + "...");

            // 3. Validar expiración y firma
            if (!jwtUtils.isTokenValid(token)) {
                System.out.println("ERROR: Token inválido o expirado");
                return onError(exchange, "Acceso denegado: Token inválido o expirado", HttpStatus.UNAUTHORIZED);
            }

            // 4. Extraer los Claims
            Claims claims = jwtUtils.extractAllClaims(token);
            
            String usernameOrEmail = claims.getSubject(); 
            String role = claims.get("role", String.class);

            System.out.println("--- GATEWAY JWT DEBUG ---");
            System.out.println("Usuario: " + usernameOrEmail);
            System.out.println("Rol: " + role);
            System.out.println("Todos los claims: " + claims);

            // 5. Inyectamos los datos en los Headers
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Document", usernameOrEmail != null ? usernameOrEmail : "")
                    .header("X-User-Role", role != null ? role : "")
                    .build();
            
            System.out.println("Headers inyectados: X-User-Role=" + role + ", X-User-Document=" + usernameOrEmail);
            System.out.println("=== FIN GATEWAY FILTER ===");

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    // ✅ AGREGAR ESTE MÉTODO - Estructura para retornar el error 401 en formato JSON
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        
        String jsonError = String.format("{\"error\": \"No autorizado\", \"mensaje\": \"%s\", \"codigo\": %d}", message, status.value());
        byte[] bytes = jsonError.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}