package epam.gymcrm.rest;

import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.service.TrainingService;
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

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Operation(
            summary = "Get trainee profile by username",
            description = "Fetches the profile information of a trainee by their username.",
            parameters = {
                    @Parameter(name = "username", example = "john.doe", description = "Username of the trainee", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee Information retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @GetMapping("/by-username")
    public ResponseEntity<TraineeProfileResponseDto> getByUsername(
           @NotBlank @NotNull @RequestParam("username") String username) {
        return ResponseEntity.ok(traineeService.getByUsername(username));
    }

    @Operation(
            summary = "Update trainee profile",
            description = "Updates the personal information (e.g., name, address) of a trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainee profile update payload",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(
            @RequestBody @Valid UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        return ResponseEntity.ok(traineeService.updateProfile(updateTraineeProfileRequestDto));
    }

    @Operation(
            summary = "Delete trainee by username",
            description = "Deletes a trainee from the system using their username.",
            parameters = {
                    @Parameter(name = "username", example = "john.doe", description = "Username of the trainee to be deleted", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteByUsername(
            @RequestParam("username") @Valid String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update trainee's trainers list",
            description = "Assign or update the list of trainers assigned to a trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of trainer usernames to be assigned to the trainee",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer list updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @PutMapping("/update-trainers-list")
    public ResponseEntity<UpdateTraineeTrainersResponseDto> updateTraineeTrainersList(
            @RequestBody @Valid UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto) {
        return ResponseEntity.ok(traineeService.updateTraineeTrainersList(updateTraineeTrainerListDto));
    }

    @Operation(
            summary = "Get trainee's training list",
            description = "Retrieves all the training sessions associated with a specific trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainee training list filter (e.g., by username, date range)",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training list retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @GetMapping("/trainings-list")
    public ResponseEntity<List<TraineeTrainingsListResponseDto>> getTrainingsList(
            @RequestBody @Valid TraineeTrainingsRequestDto trainingsRequestDto) {
        return ResponseEntity.ok(trainingService.getTraineeTrainings(trainingsRequestDto));
    }

    @Operation(
            summary = "Change trainee status (activate/deactivate)",
            description = "Activates or deactivates a trainee account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Status change payload with username and desired active status",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status changed successfully"),
                    @ApiResponse(responseCode = "404", description = "Trainee not found")
            }
    )
    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(
            @RequestBody @Valid ActivateDeactivateRequestDto statusDto) {
        traineeService.changeStatus(statusDto);
        return ResponseEntity.ok().build();
    }
}
