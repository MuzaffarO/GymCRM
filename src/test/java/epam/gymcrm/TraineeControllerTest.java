package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequest;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponse;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequest;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.controller.TraineeController;
import epam.gymcrm.facade.TraineeFacade;
import epam.gymcrm.facade.TrainingFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
    private TrainingFacade trainingFacade;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TraineeFacade traineeFacade;

    @BeforeEach
    void setup() {
        Mockito.reset(traineeFacade, trainingFacade);
    }

    @Test
    void testGetByUsername() throws Exception {
        String username = "john.doe";
        TraineeProfileResponse responseDto = new TraineeProfileResponse("John", "Doe", new Date(), "123 Street", true, Collections.emptyList());

        Mockito.when(traineeFacade.getByUsername(eq(username)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/trainees/by-username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        UpdateTraineeProfileRequest requestDto = new UpdateTraineeProfileRequest("john.doe", "John", "Doe", new Date(), "New Street", true);
        UpdateTraineeProfileResponse responseDto = new UpdateTraineeProfileResponse("john.doe", "John", "Doe", new Date(), "New Street", true, Collections.emptyList());

        Mockito.when(traineeFacade.updateProfile(any())).thenReturn(responseDto);

        mockMvc.perform(put("/trainees/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    void testDeleteByUsername() throws Exception {
        String username = "john.doe";

        doNothing().when(traineeFacade).deleteByUsername(eq(username));

        mockMvc.perform(delete("/trainees/delete")
                        .param("username", username))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateTraineeTrainersList() throws Exception {
        UpdateTraineeTrainerListRequest requestDto = new UpdateTraineeTrainerListRequest(
                "john.doe", List.of(new TrainerUsernameRequest("trainer1")));

        UpdateTraineeTrainersResponse responseDto = new UpdateTraineeTrainersResponse(Collections.emptyList());

        Mockito.when(traineeFacade.updateTraineeTrainersList(any()))
                .thenReturn(responseDto);

        mockMvc.perform(put("/trainees/update-trainers-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTrainingsList() throws Exception {
        TraineeTrainingsRequest requestDto = new TraineeTrainingsRequest("john.doe", new Date(), new Date(), null, null);
        List<TraineeTrainingsListResponse> trainingList = List.of();

        Mockito.when(traineeFacade.getTrainingsList(any()))
                .thenReturn(trainingList);

        mockMvc.perform(get("/trainees/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequest statusDto = new ActivateDeactivateRequest("john.doe", true);

        doNothing().when(traineeFacade).changeStatus(any());

        mockMvc.perform(patch("/trainees/change-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk());
    }


    @TestConfiguration
    static class MockedBeansConfig {

        @Bean
        public TraineeFacade traineeFacade() {
            return Mockito.mock(TraineeFacade.class);
        }

        @Bean
        public TrainingFacade trainingService() {
            return Mockito.mock(TrainingFacade.class);
        }
    }

}
