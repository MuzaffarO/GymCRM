package epam.gymcrm.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.microservice.TrainerWorkloadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerWorkloadProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    public void sendTrainerWorkload(TrainerWorkloadRequest request) {
        String jwtToken = extractJwtFromSecurityContextOrNull();

        try {
            String messageBody = objectMapper.writeValueAsString(request);

            Map<String, MessageAttributeValue> attrs = new HashMap<>();
            if (jwtToken != null && !jwtToken.isBlank()) {
                attrs.put("Authorization", MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("Bearer " + jwtToken)
                        .build());
            }

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .messageAttributes(attrs)
                    .build();

            var resp = sqsClient.sendMessage(sendMsgRequest);
            log.info("SQS sent to {} messageId={}", queueUrl, resp.messageId());
            var attrs1 = sqsClient.getQueueAttributes(b -> b
                    .queueUrl(queueUrl)
                    .attributeNamesWithStrings("ApproximateNumberOfMessages","ApproximateNumberOfMessagesNotVisible"));
            log.info("Queue attrs after send: visible={}, inflight={}",
                    attrs1.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES),
                    attrs1.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize TrainerWorkloadRequest", e);
        }
    }

    private String extractJwtFromSecurityContextOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getCredentials() == null) {
            return null;
        }
        return String.valueOf(auth.getCredentials());
    }
}

