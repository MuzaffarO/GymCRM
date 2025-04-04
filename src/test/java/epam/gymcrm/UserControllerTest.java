package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainee.request.TraineeRegisterDto;
import epam.gymcrm.dto.trainer.request.TrainerRegisterDto;
import epam.gymcrm.dto.trainingtype.TrainingTypeDto;
import epam.gymcrm.dto.user.response.CredentialsInfoResponseDto;
import epam.gymcrm.controller.UserController;
import epam.gymcrm.facade.UserFacade;
import epam.gymcrm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockedBeansConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterTrainer() throws Exception {
        TrainerRegisterDto dto = new TrainerRegisterDto("John", "Doe", new TrainingTypeDto(1,"karate"));
        CredentialsInfoResponseDto response = new CredentialsInfoResponseDto("john.doe", "generatedPassword");

        when(userFacade.registerTrainer(any())).thenReturn(response);

        mockMvc.perform(post("/users/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    void testRegisterTrainee() throws Exception {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000");
        TraineeRegisterDto dto = new TraineeRegisterDto("Jane", "Smith", date, "123 Street");
        CredentialsInfoResponseDto response = new CredentialsInfoResponseDto("jane.smith", "secret");

        when(userFacade.registerTrainee(any())).thenReturn(response);

        mockMvc.perform(post("/users/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jane.smith"));
    }

    @Test
    void testLogin() throws Exception {
        doNothing().when(userFacade).login("user", "pass");

        mockMvc.perform(get("/users/login")
                        .param("username", "user")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeLogin() throws Exception {
        doNothing().when(userFacade).changeLogin("user", "oldPass", "newPass");

        mockMvc.perform(put("/users/change-login")
                        .param("username", "user")
                        .param("oldPassword", "oldPass")
                        .param("newPassword", "newPass"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public UserFacade usersServices() {
            return mock(UserFacade.class);
        }
    }
}