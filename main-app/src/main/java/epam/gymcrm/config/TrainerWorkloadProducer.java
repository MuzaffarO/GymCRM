package epam.gymcrm.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.microservice.TrainerWorkloadRequest;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerWorkloadProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private static final String QUEUE_NAME = "trainer.workload.queue";

    public void sendTrainerWorkload(TrainerWorkloadRequest request) {
//        jmsTemplate.convertAndSend(QUEUE_NAME, request);
        String jwtToken = extractJwtFromSecurityContext();
        jmsTemplate.send("trainer.workload.queue", session -> {
            Message message = null;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            message.setStringProperty("Authorization", "Bearer " + jwtToken);
            return message;
        });

    }
    public String extractJwtFromSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getCredentials() == null) {
            throw new IllegalStateException("JWT Token not available in security context");
        }
        return auth.getCredentials().toString(); // credentials hold the raw JWT
    }
}
