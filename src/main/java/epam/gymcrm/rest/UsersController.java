package epam.gymcrm.rest;

import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.service.UsersServices;
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
public class UsersController {

    private final UsersServices usersServices;

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
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainer(
            @Valid @RequestBody TrainerRegisterDto trainerRegisterDto) {
        return usersServices.registerTrainer(trainerRegisterDto);
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
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainee(
            @Valid @RequestBody TraineeRegisterDto traineeRegisterDto) {
        return usersServices.registerTrainee(traineeRegisterDto);
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
        return usersServices.login(username, password);
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
        return usersServices.changeLogin(username, oldPassword, newPassword);
    }
}
