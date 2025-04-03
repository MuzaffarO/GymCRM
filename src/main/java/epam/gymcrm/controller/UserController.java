package epam.gymcrm.controller;

import epam.gymcrm.dto.trainee.request.TraineeRegisterRequest;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @Operation(
            summary = "Register a new trainer",
            description = "Registers a new trainer and returns credentials info.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer registration data",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid trainer data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/trainer/register")
    public ResponseEntity<CredentialsInfoResponse> registerTrainer(
            @Valid @RequestBody TrainerRegister trainerRegister) {
        return ResponseEntity.ok(userFacade.registerTrainer(trainerRegister));
    }

    @Operation(
            summary = "Register a new trainee",
            description = "Registers a new trainee and returns credentials info.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainee registration data",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid trainee data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/trainee/register")
    public ResponseEntity<CredentialsInfoResponse> registerTrainee(
            @Valid @RequestBody TraineeRegisterRequest traineeRegisterRequest) {
        return ResponseEntity.ok(userFacade.registerTrainee(traineeRegisterRequest));
    }

    @Operation(
            summary = "User login",
            description = "Verifies username and password for login.",
            parameters = {
                    @Parameter(name = "username", description = "User's username", required = true, example = "muzaffar.obidjonov"),
                    @Parameter(name = "password", description = "User's password", required = true, example = "12345678")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {
        userFacade.login(username, password);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change user password",
            description = "Changes the password for an existing user.",
            parameters = {
                    @Parameter(name = "username", description = "User's username", required = true, example = "muzaffar.obidjonov"),
                    @Parameter(name = "oldPassword", description = "Current password", required = true, example = "12345678"),
                    @Parameter(name = "newPassword", description = "New password", required = true, example = "qwerty123")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or wrong old password"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "oldPassword") String oldPassword,
            @RequestParam(name = "newPassword") String newPassword) {
        userFacade.changeLogin(username, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }
}
