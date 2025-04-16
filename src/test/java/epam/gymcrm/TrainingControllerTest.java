package epam.gymcrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.controller.TrainingController;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
@Import({TrainingControllerTest.MockedBeansConfig.class, TrainingControllerTest.NoSecurityConfig.class})
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingFacade trainingFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private TrainingRegister trainingRegister;

    @BeforeEach
    void setUp() throws Exception {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("29/03/2025");
        trainingRegister = new TrainingRegister("trainee1", "trainer1", "karate", date, 1.5);
    }

    @Test
    void testCreateTraining_Success() throws Exception {
        doNothing().when(trainingFacade).createTraining(any());

        mockMvc.perform(post("/trainings/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRegister)))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBeansConfig {
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
