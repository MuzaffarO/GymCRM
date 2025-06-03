package epam.uz.trainerworkloadservice;

import epam.uz.trainerworkloadservice.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testGenerateAndValidateToken() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000)) // 1 min
                .signWith(Keys.hmacShaKeyFor("vR7xP9m$Jk3!qW@fYzL2bNcT#H8sAe4D".getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("testuser", jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "bad.token.value";
        assertFalse(jwtUtil.validateToken(invalidToken));
    }
}
