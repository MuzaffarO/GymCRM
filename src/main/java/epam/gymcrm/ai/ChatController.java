package epam.gymcrm.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final GeminiChatService geminiChatService;

    @PostMapping
    public String chat(@RequestBody String userInput) {
        return geminiChatService.chat(userInput);
    }
}
