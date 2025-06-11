package epam.uz.trainerworkloadservice.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
@Getter
public class DlqMessageStore {

    private final Queue<String> failedMessages = new ConcurrentLinkedQueue<>();

    public void add(String msg) {
        failedMessages.add(msg);
        log.warn("Stored DLQ message for retry later: {}", msg);
    }

    public void clear() {
        failedMessages.clear();
    }
}
