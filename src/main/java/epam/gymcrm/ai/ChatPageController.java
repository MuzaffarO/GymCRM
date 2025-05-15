package epam.gymcrm.ai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatPageController {

    @GetMapping("/chat-ui")
    public String chatPage() {
        return "chat";
    }
}
