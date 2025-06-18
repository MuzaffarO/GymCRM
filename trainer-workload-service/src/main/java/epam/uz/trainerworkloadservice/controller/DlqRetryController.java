package epam.uz.trainerworkloadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.DlqMessageStore;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@Slf4j
@RestController
@RequestMapping("/api/dlq")
@RequiredArgsConstructor
public class DlqRetryController {

    private final DlqMessageStore dlqMessageStore;
    private final TrainerMongoWorkloadService workloadService;
    private final ObjectMapper objectMapper;

    @PostMapping("/retry")
    public String retryFailedMessages(@RequestHeader(value = "X-Transaction-Id", required = false) String txnId) {
        int success = 0;
        int failed = 0;

        Iterator<String> iterator = dlqMessageStore.getFailedMessages().iterator();

        while (iterator.hasNext()) {
            String message = iterator.next();
            try {
                TrainerWorkloadRequest request = objectMapper.readValue(message, TrainerWorkloadRequest.class);
                log.info("[{}] Retrying DLQ message for trainer: {}", txnId, request.getTrainerUsername());
                workloadService.processWorkload(request); // txnId can be added to the method if needed
                success++;
                iterator.remove();
            } catch (Exception e) {
                log.error("[{}] Failed to reprocess DLQ message: {}", txnId, message, e);
                failed++;
            }
        }

        return "✅ Retried messages: " + success + ", ❌ Failed: " + failed;
    }
}
