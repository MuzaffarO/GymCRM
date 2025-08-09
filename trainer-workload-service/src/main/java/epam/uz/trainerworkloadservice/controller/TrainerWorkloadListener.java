package epam.uz.trainerworkloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerWorkloadListener {

    private final TrainerMongoWorkloadService workloadService;
    private final ObjectMapper objectMapper;
    private final Key hmacKey;

    @SqsListener("${app.sqs.main-queue-url}")
    public void receiveMessage(@Payload String body, @org.springframework.messaging.handler.annotation.Header(name = "Authorization", required = false) String jwtAttr, @Headers Map<String, String> headers) {

        String txnId = "TXN-" + System.currentTimeMillis();
        try {
            log.info("[{}] Raw SQS Message: {}", txnId, body);

            if (jwtAttr == null || !jwtAttr.startsWith("Bearer ")) {
                throw new SecurityException("Missing or invalid Authorization attribute");
            }
            String jwt = jwtAttr.substring(7);

            Claims claims = Jwts.parserBuilder().setSigningKey(hmacKey) // injected bean, see below
                    .build().parseClaimsJws(jwt).getBody();

            String user = claims.getSubject();
            log.info("[{}] JWT verified for user: {}", txnId, user);

            TrainerWorkloadRequest request = objectMapper.readValue(body, TrainerWorkloadRequest.class);

            log.info("[{}] Processing workload for trainer: {}", txnId, request.getTrainerUsername());
            workloadService.processWorkload(request);
            log.info("[{}] Successfully processed workload", txnId);
            log.info("[{}] DTO -> user='{}', first='{}', last='{}', date={}, dur={}, action={}", txnId, request.getTrainerUsername(), request.getTrainerFirstName(), request.getTrainerLastName(), request.getTrainingDate(), request.getTrainingDuration(), request.getActionType());

        } catch (Exception e) {
            log.error("[{}] Error handling SQS message: {}", txnId, e.getMessage(), e);
            throw new RuntimeException("Unauthorized or invalid message", e);
        }
    }

    @SqsListener("https://sqs.us-east-1.amazonaws.com/663264486623/MyApp-Queue-DLQ")
    public void handleDLQ(@Payload String message) {
        log.warn("Message sent to DLQ: {}", message);
    }
}
