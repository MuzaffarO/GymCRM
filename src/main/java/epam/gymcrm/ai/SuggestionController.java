//package epam.gymcrm.ai;
//
//import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
//import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
//import epam.gymcrm.facade.TrainingFacade;
//import epam.gymcrm.model.TrainingType;
//import epam.gymcrm.repository.TrainingTypeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class SuggestionController {
//
//    private final GeminiChatService geminiChatService;
//    private final TrainingFacade trainingFacade; // you'll need to implement this
//    private final TrainingTypeRepository trainingTypeRepository;
//
//    @GetMapping("/suggestions")
//    public String showSuggestions(@RequestParam String username, Model model) {
//        List<TraineeTrainingsListResponse> pastTrainings = trainingFacade.getTraineeTrainings(new TraineeTrainingsRequest(username));
//        List<String> availableTrainings = trainingTypeRepository.findAll()
//            .stream().map(TrainingType::getTrainingTypeName).toList();
//
//        String prompt = """
//            The user has attended the following training types in the past: %s.
//            Currently available training types are: %s.
//
//            Suggest a 3-day training plan and recommend 1–2 new training types from the list that align well with the past history. Justify briefly.
//        """.formatted(String.join(", ", pastTrainings), String.join(", ", availableTrainings));
//
//        String aiResponse = geminiChatService.chat(prompt);
//        model.addAttribute("username", username);
//        model.addAttribute("suggestion", aiResponse);
//
//        return "ai-suggestions"; // Thymeleaf template
//    }
//}
