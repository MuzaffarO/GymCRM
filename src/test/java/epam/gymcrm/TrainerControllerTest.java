package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponseDto;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponseDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.user.request.SpecializationNameDto;
import epam.gymcrm.controller.TrainerController;
import epam.gymcrm.facade.TrainerFacade;
import epam.gymcrm.facade.TrainingFacade;
import epam.gymcrm.service.TrainerService;
import epam.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
        TrainerProfileResponseDto dto = new TrainerProfileResponseDto("John", "Doe", null, true, null);
        when(trainerFacade.getByUsername(eq(username))).thenReturn(dto);

        mockMvc.perform(get("/trainers/by-username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        UpdateTrainerProfileRequestDto request = new UpdateTrainerProfileRequestDto("John.Doe", "John", "Doe", new SpecializationNameDto("karate"), true);
        UpdateTrainerProfileResponseDto response = new UpdateTrainerProfileResponseDto("John.Doe", "John", "Doe", new SpecializationNameDto("karate"), true, null);
        when(trainerFacade.updateProfile(any())).thenReturn(response);

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
        when(trainerFacade.getNotAssignedActiveTrainers(eq(username))).thenReturn(List.of(trainer));

        mockMvc.perform(get("/trainers/not-assigned-active")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"));
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
        TrainerTrainingsRequestDto request = new TrainerTrainingsRequestDto("trainer1", new Date(), new Date(), null);
        List<TrainerTrainingsListResponseDto> responseList = List.of();
        when(trainingFacade.getTrainerTrainings(any())).thenReturn(responseList);

        mockMvc.perform(get("/trainers/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequestDto request = new ActivateDeactivateRequestDto("trainer1", false);
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
    }
}