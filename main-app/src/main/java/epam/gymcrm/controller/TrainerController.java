package epam.gymcrm.controller;

import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponse;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.facade.TrainerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/trainers")
@RestController
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerFacade trainerFacade;

    @Operation(
            summary = "Get trainer profile by username",
            description = "Fetches the profile of a trainer using their username.",
            parameters = {
                    @Parameter(name = "username", description = "Username of the trainer to fetch the profile for", example = "john.doe", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    @GetMapping("/by-username")
    public ResponseEntity<TrainerProfileResponse> getByUsername(
            @NotNull @NotBlank @RequestParam("username") String username) {
        return ResponseEntity.ok(trainerFacade.getByUsername(username));
    }

    @Operation(
            summary = "Update trainer profile",
            description = "Updates the profile details of a trainer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer profile update payload",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTrainerProfileResponse> updateProfile(
            @RequestBody @Valid UpdateTrainerProfileRequest updateTrainerProfileRequest) {
        return ResponseEntity.ok(trainerFacade.updateProfile(updateTrainerProfileRequest));
    }

    @Operation(
            summary = "Get all active trainers not assigned to a specific trainee",
            description = "Returns a list of active trainers that are not yet assigned to the specified trainee (by username).",
            parameters = {
                    @Parameter(name = "username", description = "Username of the trainee to check for unassigned trainers", example = "john.doe", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of unassigned active trainers retrieved successfully")
            }
    )
    @GetMapping("/not-assigned-active")
    public ResponseEntity<List<TrainerResponse>> getNotAssignedActiveTrainers(
            @NotBlank @NotNull @RequestParam("username") String username) {
        return ResponseEntity.ok(trainerFacade.getNotAssignedActiveTrainers(username));
    }

    @Operation(
            summary = "Get list of trainings by trainer",
            description = "Fetches a list of trainings conducted by a specific trainer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer trainings list request payload (e.g., username, date filter)",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training list retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    @GetMapping("/trainings-list")
    public ResponseEntity<List<TrainerTrainingsListResponse>> getTrainerTrainings(
            @RequestBody @Valid TrainerTrainingsRequest trainerTrainingsRequest) {
        return ResponseEntity.ok(trainerFacade.getTrainerTrainings(trainerTrainingsRequest));
    }

    @Operation(
            summary = "Change trainer active status (activate/deactivate)",
            description = "Activates or deactivates a trainer account using their username.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer status change request (username and new status)",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainer not found")
            }
    )
    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(
            @RequestBody @Valid ActivateDeactivateRequest statusDto) {
        trainerFacade.changeStatus(statusDto);
        return ResponseEntity.ok().build();
    }
}
