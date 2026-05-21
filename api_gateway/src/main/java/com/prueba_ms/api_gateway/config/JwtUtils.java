package com.prueba_ms.api_gateway.config;


import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    // Usamos la misma clave oficial en Base64
    private static final String SECRET_KEY_STRING = "efmycl3ZY1LasnSGalvccYA1nR5Yo3r9WdWYoHft8rY=";
    
    // CORRECCIÓN AQUÍ: Decodificamos en BASE64 igual que en el JwtService de usuarios
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING));

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new java.util.Date());
        } catch (Exception e) {
            System.out.println("--- GATEWAY JWT ERROR --- Firma o token inválido: " + e.getMessage());
            return false;
        }
    }
}