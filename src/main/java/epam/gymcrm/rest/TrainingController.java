package epam.gymcrm.rest;

import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/trainings")
@RestController
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @Operation(
            summary = "Create a new training",
            description = "Registers a new training session for a trainee with a specific trainer and training type.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training registration data including trainee username, trainer username, training name, date, and duration",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "404", description = "Trainee or trainer or training type not found"),
                    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<Void> createTraining(@RequestBody @Valid TrainingRegisterDto trainingRegisterDto) {
        trainingService.createTraining(trainingRegisterDto);
        return ResponseEntity.ok().build();
    }
}
