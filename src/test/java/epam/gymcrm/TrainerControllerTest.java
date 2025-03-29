package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.request.*;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.rest.TrainerController;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.TrainingServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
@Import(TrainerControllerTest.MockedBeansConfig.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerServices trainerServices;

    @Autowired
    private TrainingServices trainingServices;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reset(trainerServices, trainingServices);
    }

    @Test
    void testGetByUsername() throws Exception {
        String username = "john.doe";
        TrainerProfileResponseDto dto = new TrainerProfileResponseDto("John", "Doe", null, true, null);
        when(trainerServices.getByUsername(eq(username))).thenReturn(ResponseEntity.ok(dto));

        mockMvc.perform(get("/trainers/by-username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        UpdateTrainerProfileRequestDto request = new UpdateTrainerProfileRequestDto("John.Doe", "John", "Doe", new SpecializationNameDto("karate"), true);
        UpdateTrainerProfileResponseDto response = new UpdateTrainerProfileResponseDto("John.Doe", "John", "Doe", new SpecializationNameDto("karate"), true, null);
        when(trainerServices.updateProfile(any())).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(put("/trainers/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John.Doe"));
    }

    @Test
    void testGetNotAssignedActiveTrainers() throws Exception {
        String username = "john.doe";
        TrainerResponseDto trainer = new TrainerResponseDto("trainer1", "Trainer", "One", null);
        when(trainerServices.getNotAssignedActiveTrainers(eq(username))).thenReturn(ResponseEntity.ok(List.of(trainer)));

        mockMvc.perform(get("/trainers/not-assigned-active")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"));
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        TrainerTrainingsRequestDto request = new TrainerTrainingsRequestDto("trainer1", new Date(), new Date(), null);
        List<TrainerTrainingsListResponseDto> responseList = List.of();
        when(trainingServices.getTrainerTrainings(any())).thenReturn(ResponseEntity.ok(responseList));

        mockMvc.perform(get("/trainers/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequestDto request = new ActivateDeactivateRequestDto("trainer1", false);
        when(trainerServices.changeStatus(any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/trainers/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TrainerServices trainerServices() {
            return mock(TrainerServices.class);
        }

        @Bean
        public TrainingServices trainingServices() {
            return mock(TrainingServices.class);
        }
    }
}