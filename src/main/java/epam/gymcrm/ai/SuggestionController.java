package epam.gymcrm.ai;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SuggestionController {

    private final GeminiChatService geminiChatService;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingService trainingService;

    @GetMapping("/suggestions")
    public String showSuggestions(@RequestParam String username, Model model) {
        List<TraineeTrainingsListResponse> pastTrainings = trainingService.getTraineeTrainings(new TraineeTrainingsRequest(username));
        List<String> availableTrainings = trainingTypeRepository.findAll()
            .stream().map(TrainingType::getTrainingTypeName).toList();

        StringBuilder historyBuilder = new StringBuilder();
        for (TraineeTrainingsListResponse training : pastTrainings) {
            historyBuilder.append(String.format("- %s (%s hours) on %s with trainer %s (Type: %s)\n",
                    training.getTrainingName(),
                    training.getTrainingDuration(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(training.getTrainingDate()),
                    training.getTrainerName(),
                    training.getTrainingType()
            ));
        }

        String prompt = """
        The following is the user's training history:
        %s

        Available training types at the gym are: %s.

        Please suggest a personalized 3-day training plan based on the user's previous exercises and durations. 
        Also, recommend one or two new training types that align well with their training history.
        Explain your reasoning briefly, and keep it trainee-friendly.
    """.formatted(historyBuilder.toString(), String.join(", ", availableTrainings));

        String aiResponse = geminiChatService.chat(prompt);
        model.addAttribute("username", username);
        model.addAttribute("suggestion", aiResponse);

        return "ai-suggestions";
    }
}
