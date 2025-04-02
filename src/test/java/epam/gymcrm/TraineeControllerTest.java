package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.request.*;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.rest.TraineeController;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TraineeController.class)
@Import(TraineeControllerTest.MockedBeansConfig.class)
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Mockito.reset(traineeService, trainingService);
    }

    @Test
    void testGetByUsername() throws Exception {
        String username = "john.doe";
        TraineeProfileResponseDto responseDto = new TraineeProfileResponseDto("John", "Doe", new Date(), "123 Street", true, Collections.emptyList());

        Mockito.when(traineeService.getByUsername(eq(username)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/trainees/by-username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        UpdateTraineeProfileRequestDto requestDto = new UpdateTraineeProfileRequestDto("john.doe", "John", "Doe", new Date(), "New Street", true);
        UpdateTraineeProfileResponseDto responseDto = new UpdateTraineeProfileResponseDto("john.doe", "John", "Doe", new Date(), "New Street", true, Collections.emptyList());

        Mockito.when(traineeService.updateProfile(any())).thenReturn(responseDto);

        mockMvc.perform(put("/trainees/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    void testDeleteByUsername() throws Exception {
        String username = "john.doe";

        doNothing().when(traineeService).deleteByUsername(eq(username));

        mockMvc.perform(delete("/trainees/delete")
                        .param("username", username))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateTraineeTrainersList() throws Exception {
        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto(
                "john.doe", List.of(new TrainerUsernameRequestDto("trainer1")));

        UpdateTraineeTrainersResponseDto responseDto = new UpdateTraineeTrainersResponseDto(Collections.emptyList());

        Mockito.when(traineeService.updateTraineeTrainersList(any()))
                .thenReturn(responseDto);

        mockMvc.perform(put("/trainees/update-trainers-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainingsList() throws Exception {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto("john.doe", new Date(), new Date(), null, null);
        List<TraineeTrainingsListResponseDto> trainingList = List.of();

        Mockito.when(trainingService.getTraineeTrainings(any()))
                .thenReturn(trainingList);

        mockMvc.perform(get("/trainees/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequestDto statusDto = new ActivateDeactivateRequestDto("john.doe", true);

        doNothing().when(traineeService).changeStatus(any());

        mockMvc.perform(patch("/trainees/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk());
    }


    // Test configuration to provide mocks instead of @MockBean
    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TraineeService traineeServices() {
            return Mockito.mock(TraineeService.class);
        }

        @Bean
        public TrainingService trainingServices() {
            return Mockito.mock(TrainingService.class);
        }
    }
}
