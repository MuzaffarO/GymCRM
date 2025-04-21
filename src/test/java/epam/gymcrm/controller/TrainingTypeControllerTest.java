package epam.gymcrm.controller;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.facade.TrainingTypeFacade;
import epam.gymcrm.model.TrainingType;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingTypeController.class)
@Import({TrainingTypeControllerTest.MockedBeansConfig.class, TrainingTypeControllerTest.NoSecurityConfig.class})
class TrainingTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingTypeFacade trainingTypeFacade;

    @Test
    void testGetTrainingType() throws Exception {
        List<TrainingTypeDTO> mockList = List.of(new TrainingTypeDTO(1,"karate"));
        when(trainingTypeFacade.getTrainingTypes()).thenReturn(mockList);

        mockMvc.perform(get("/training-type"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTrainingType() throws Exception {
        TrainingType trainingType = TrainingType.builder()
                .id(1)
                .trainingTypeName("yoga")
                .build();

        when(trainingTypeFacade.createTrainingType("yoga")).thenReturn(trainingType);

        mockMvc.perform(post("/training-type")
                        .param("name", "yoga")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public TrainingTypeFacade trainingTypeServices() {
            return mock(TrainingTypeFacade.class);
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