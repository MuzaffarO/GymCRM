package epam.uz.trainerworkloadservice.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
class JwtConfig {
  @Bean
  Key hmacKey(@Value("${security.jwt.hmacSecret}") String secret) {
    return Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
  }
}
