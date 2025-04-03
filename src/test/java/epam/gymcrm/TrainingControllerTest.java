package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.training.request.TrainingRegisterDto;
import epam.gymcrm.controller.TrainingController;
import epam.gymcrm.facade.TrainingFacade;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
@Import(TrainingControllerTest.MockedBeansConfig.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingFacade trainingFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private TrainingRegisterDto trainingRegisterDto;

    @BeforeEach
    void setUp() throws Exception {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("29/03/2025");
        trainingRegisterDto = new TrainingRegisterDto("trainee1", "trainer1", "karate", date, 1.5);
    }

    @Test
    void testCreateTraining_Success() throws Exception {
        doNothing().when(trainingFacade).createTraining(any());

        mockMvc.perform(post("/trainings/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRegisterDto)))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TrainingFacade trainingServices() {
            return mock(TrainingFacade.class);
        }
    }
}