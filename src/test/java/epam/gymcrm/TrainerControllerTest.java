package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponse;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.controller.TrainerController;
import epam.gymcrm.facade.TrainerFacade;
import epam.gymcrm.facade.TrainingFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
@Import({TrainerControllerTest.MockedBeansConfig.class, TrainerControllerTest.NoSecurityConfig.class})
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerFacade trainerFacade;

    @Autowired
    private TrainingFacade trainingFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reset(trainerFacade, trainingFacade);
    }

    @Test
    void testGetByUsername() throws Exception {
        String username = "john.doe";
        TrainerProfileResponse dto = new TrainerProfileResponse("John", "Doe", null, true, null);
        when(trainerFacade.getByUsername(eq(username))).thenReturn(dto);

        mockMvc.perform(get("/trainers/by-username")
                        .param("username", username))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProfile() throws Exception {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest("John.Doe", "John", "Doe", new SpecializationName("karate"), true);
        UpdateTrainerProfileResponse response = new UpdateTrainerProfileResponse("John.Doe", "John", "Doe", new SpecializationName("karate"), true, null);
        when(trainerFacade.updateProfile(any())).thenReturn(response);

        mockMvc.perform(put("/trainers/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNotAssignedActiveTrainers() throws Exception {
        String username = "john.doe";
        TrainerResponse trainer = new TrainerResponse("trainer1", "Trainer", "One", null);
        when(trainerFacade.getNotAssignedActiveTrainers(eq(username))).thenReturn(List.of(trainer));

        mockMvc.perform(get("/trainers/not-assigned-active")
                        .param("username", username))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        TrainerTrainingsRequest request = new TrainerTrainingsRequest("trainer1", new Date(), new Date(), null);
        List<TrainerTrainingsListResponse> responseList = List.of();
        when(trainingFacade.getTrainerTrainings(any())).thenReturn(responseList);

        mockMvc.perform(get("/trainers/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequest request = new ActivateDeactivateRequest("trainer1", false);
        doNothing().when(trainerFacade).changeStatus(any());

        mockMvc.perform(patch("/trainers/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TrainerFacade trainerServices() {
            return mock(TrainerFacade.class);
        }

        @Bean
        public TrainingFacade trainingServices() {
            return mock(TrainingFacade.class);
        }
        @Bean
        public epam.gymcrm.security.JwtUtil jwtUtil() {
            return Mockito.mock(epam.gymcrm.security.JwtUtil.class);
        }
        @Bean
        public epam.gymcrm.security.JwtAuthFilter jwtAuthFilter() {
            return Mockito.mock(epam.gymcrm.security.JwtAuthFilter.class);
        }
    }

    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );

            return http.build();
        }
    }
}