package epam.uz.trainerworkloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.DlqMessageStore;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
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

    private final TrainerMongoWorkloadService workloadService;
    private final ObjectMapper objectMapper;
    private final DlqMessageStore dlqMessageStore;

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveMessage(Message message) {
        String txnId = "TXN-" + System.currentTimeMillis();

        try {
            if (!(message instanceof TextMessage textMessage)) {
                throw new IllegalArgumentException("Unsupported message type");
            }

            String jwt = message.getStringProperty("Authorization");
            if (jwt == null || !jwt.startsWith("Bearer ")) {
                throw new SecurityException("Missing or invalid Authorization header");
            }

            jwt = jwt.substring(7); // Strip "Bearer "

            // Key must be at least 256 bits (32 bytes)
            String secret = "vR7xP9m$Jk3!qW@fYzL2bNcT#H8sAe4D";
            Key hmacKey = Keys.hmacShaKeyFor(secret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String user = claims.getSubject();
            log.info("[{}] JWT verified for user: {}", txnId, user);

            TrainerWorkloadRequest request = objectMapper.readValue(textMessage.getText(), TrainerWorkloadRequest.class);

            log.info("[{}] Received workload for trainer: {}", txnId, request.getTrainerUsername());

            workloadService.processWorkload(request); // You can also pass txnId if needed

            log.info("[{}] Successfully processed workload for trainer: {}", txnId, request.getTrainerUsername());

        } catch (Exception e) {
            log.error("[{}] Failed to process JMS message: {}", txnId, e.getMessage(), e);
            throw new RuntimeException("Unauthorized or invalid message", e); // triggers DLQ
        }
    }

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void listenToDeadLetterQueue(String message) {
        log.warn("Message sent to Dead Letter Queue: {}", message);
        dlqMessageStore.add(message);
    }

    // For testing:
    // @JmsListener(destination = "trainer.workload.queue")
    // public void simulateFailure(Message message) {
    //     throw new RuntimeException("Simulated failure");
    // }
}
