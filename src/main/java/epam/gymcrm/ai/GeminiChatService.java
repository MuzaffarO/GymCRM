package epam.gymcrm.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
            .build();

    public String chat(String message) {
        Map<String, Object> request = Map.of(
                "contents", new Object[] {
                        // System instruction to restrict scope
                        Map.of("role", "user", "parts", new Object[] {
                                Map.of("text", "You are a fitness assistant. Only answer training-related questions. " +
                                        "If the user asks for video references, suggest search-friendly YouTube video titles and links from real videos")
                        }),
                        // Actual user message
                        Map.of("role", "user", "parts", new Object[] {
                                Map.of("text", message)
                        })
                }
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var candidates = (java.util.List<Map<String, Object>>) response.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        var content = (Map<String, Object>) candidates.get(0).get("content");
                        var parts = (java.util.List<Map<String, Object>>) content.get("parts");
                        return (String) parts.get(0).get("text");
                    }
                    return "No response from AI.";
                })
                .block();
    }

}
