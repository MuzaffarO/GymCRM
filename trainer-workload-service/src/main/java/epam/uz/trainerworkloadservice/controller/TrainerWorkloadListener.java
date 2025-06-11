package epam.uz.trainerworkloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.DlqMessageStore;
import epam.uz.trainerworkloadservice.service.TrainerWorkloadService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerWorkloadListener {

    private final TrainerWorkloadService workloadService;
    private final ObjectMapper objectMapper;
    private final DlqMessageStore dlqMessageStore;

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveMessage(Message message) {
        try {
            if (!(message instanceof TextMessage textMessage)) {
                throw new IllegalArgumentException("Unsupported message type");
            }

            String jwt = message.getStringProperty("Authorization");
            if (jwt == null || !jwt.startsWith("Bearer ")) {
                throw new SecurityException("Missing or invalid Authorization header");
            }

            jwt = jwt.substring(7); // Remove "Bearer "

            String secret = "vR7xP9m$Jk3!qW@fYzL2bNcT#H8sAe4D"; // Should be at least 256 bits for HS256
            Key hmacKey = Keys.hmacShaKeyFor(secret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();


            log.info("JWT subject (user): {}", claims.getSubject());

            TrainerWorkloadRequest request = objectMapper.readValue(textMessage.getText(), TrainerWorkloadRequest.class);
            workloadService.processWorkload(request);

            log.info("Processed workload for trainer: {}", request.getTrainerUsername());

        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
            throw new RuntimeException("Unauthorized or invalid message", e); // will go to DLQ if needed
        }
    }

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void listenToDeadLetterQueue(String message) {
        log.error("Message sent to Dead Letter Queue: {}", message);
        dlqMessageStore.add(message);
    }

//    @JmsListener(destination = "trainer.workload.queue")
//    public void receiveMessage(Message message) {
//        throw new RuntimeException("Simulated failure for DLQ test");
//    }

}
