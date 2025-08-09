package epam.uz.trainerworkloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.TrainerDynamoWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dlq")
@RequiredArgsConstructor
public class DlqRetryController {

    private final SqsClient sqsClient;
    private final TrainerDynamoWorkloadService workloadService;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.dlqUrl}")
    private String dlqUrl;

    @PostMapping("/retry")
    public String retryDlqMessages(@RequestHeader(value = "X-Transaction-Id", required = false) String txnId) {
        int success = 0, failed = 0;
        log.info("[{}] 🟡 Starting DLQ message retry", txnId);

        while (true) {
            var receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(dlqUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(10)
                    .visibilityTimeout(60)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
            if (messages.isEmpty()) break;

            for (Message msg : messages) {
                try {
                    TrainerWorkloadRequest request = objectMapper.readValue(msg.body(), TrainerWorkloadRequest.class);
                    log.info("[{}] 🟢 Retrying workload for trainer: {}", txnId, request.getTrainerUsername());

                    workloadService.processWorkload(request);

                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(dlqUrl)
                            .receiptHandle(msg.receiptHandle())
                            .build());
                    success++;
                } catch (Exception e) {
                    log.error("[{}] 🔴 Failed to reprocess DLQ message: {}", txnId, msg.body(), e);
                    failed++;
                }
            }
        }

        log.info("[{}] ✅ Retry complete. Success: {}, Failed: {}", txnId, success, failed);
        return "✅ Retried messages: " + success + ", ❌ Failed: " + failed;
    }

}
