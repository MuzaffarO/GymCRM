package epam.gymcrm;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.facade.TrainingTypeFacade;
import epam.gymcrm.controller.TrainingTypeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingTypeController.class)
@Import(TrainingTypeDTOControllerTest.MockedBeansConfig.class)
class TrainingTypeDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingTypeFacade trainingTypeFacade;

    @Test
    void testGetTrainingType() throws Exception {
        List<TrainingTypeDTO> mockList = List.of(new TrainingTypeDTO(1,"karate"));
        when(trainingTypeFacade.getAllTrainingTypes()).thenReturn(mockList);

        mockMvc.perform(get("/training-type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeName").value("karate"));
    }

    @Test
    void testCreateTrainingType() throws Exception {
        epam.gymcrm.model.TrainingType trainingType = new epam.gymcrm.model.TrainingType();
        trainingType.setTrainingTypeName("yoga");

        when(trainingTypeFacade.createTrainingType(anyString())).thenReturn(trainingType);

        mockMvc.perform(post("/training-type")
                        .param("name", "yoga")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainingTypeName").value("yoga"));
    }

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TrainingTypeFacade trainingTypeServices() {
            return mock(TrainingTypeFacade.class);
        }
    }
}