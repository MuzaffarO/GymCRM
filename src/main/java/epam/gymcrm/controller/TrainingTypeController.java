package epam.gymcrm.controller;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.facade.TrainingTypeFacade;
import epam.gymcrm.model.TrainingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/training-type")
@RestController
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeFacade trainingTypeFacade;

    @Operation(
            summary = "Get all training types",
            description = "Retrieves a list of all available training types.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training types retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
            }
    )
    @GetMapping
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingType() {
        return ResponseEntity.ok(trainingTypeFacade.getTrainingTypes());
    }

    @Operation(
            summary = "Create a new training type",
            description = "Creates a new training type with the given name.",
            parameters = {
                    @Parameter(name = "name", description = "Name of the new training type", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training type created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid or empty training type name"),
                    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
            }
    )
    @PostMapping
    public ResponseEntity<TrainingType> createTrainingType(@NotEmpty @RequestParam String name) {
        return ResponseEntity.ok(trainingTypeFacade.createTrainingType(name));
    }
}
