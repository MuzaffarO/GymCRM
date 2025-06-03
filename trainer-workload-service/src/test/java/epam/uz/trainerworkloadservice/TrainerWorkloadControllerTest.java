package epam.uz.trainerworkloadservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import epam.uz.trainerworkloadservice.controller.TrainerWorkloadController;
import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.security.JwtAuthFilter;
import epam.uz.trainerworkloadservice.security.JwtUtil;
import epam.uz.trainerworkloadservice.security.SecurityConfig;
import epam.uz.trainerworkloadservice.service.TrainerWorkloadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerWorkloadController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class})
@ContextConfiguration(classes = {TrainerWorkloadController.class, JwtUtilTestConfig.class})
class TrainerWorkloadControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerWorkloadService workloadService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper;

    TrainerWorkloadControllerTest() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testRecordWorkload() throws Exception {
        // Arrange
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 5, 1))
                .trainingDuration(2.5)
                .actionType(ActionType.ADD)
                .build();

        String token = "dummy";

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn("john");

        // Act & Assert
        mockMvc.perform(post("/api/workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
@TestConfiguration
class JwtUtilTestConfig {
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil() {
            @Override
            public boolean validateToken(String token) {
                return "dummy".equals(token);
            }

            @Override
            public String getUsernameFromToken(String token) {
                return "john";
            }
        };
    }
}
