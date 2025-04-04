package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponseDto;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequestDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.controller.TraineeController;
import epam.gymcrm.facade.TraineeFacade;
import epam.gymcrm.facade.TrainingFacade;
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
        TraineeProfileResponseDto responseDto = new TraineeProfileResponseDto("John", "Doe", new Date(), "123 Street", true, Collections.emptyList());

        Mockito.when(traineeFacade.getByUsername(eq(username)))
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
        UpdateTraineeTrainerListRequestDto requestDto = new UpdateTraineeTrainerListRequestDto(
                "john.doe", List.of(new TrainerUsernameRequestDto("trainer1")));

        UpdateTraineeTrainersResponseDto responseDto = new UpdateTraineeTrainersResponseDto(Collections.emptyList());

        Mockito.when(traineeFacade.updateTraineeTrainersList(any()))
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

        Mockito.when(traineeFacade.getTrainingsList(any()))
                .thenReturn(trainingList);

        mockMvc.perform(get("/trainees/trainings-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatus() throws Exception {
        ActivateDeactivateRequestDto statusDto = new ActivateDeactivateRequestDto("john.doe", true);

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
