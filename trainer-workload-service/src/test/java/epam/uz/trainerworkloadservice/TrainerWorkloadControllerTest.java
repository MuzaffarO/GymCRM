package epam.uz.trainerworkloadservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.controller.TrainerWorkloadController;
import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.security.JwtUtil;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerWorkloadController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerWorkloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerMongoWorkloadService service;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;


    @Test
    void testRecordWorkload() throws Exception {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john.doe")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2025, 6, 1))
                .trainingDuration(1.5)
                .actionType(ActionType.ADD)
                .build();

        mockMvc.perform(post("/api/workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Transaction-Id", "TXN-123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trainer workload processed successfully."));
    }

    @Test
    void testGetMonthlySummary() throws Exception {
        Mockito.when(service.getMonthlyHours("john.doe", 2025, 6))
                .thenReturn(5.5);

        mockMvc.perform(get("/api/workload/john.doe/2025/6"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.5"));
    }

    @Test
    void testGetTrainerMonthlySummary() throws Exception {
        TrainerMonthlySummaryDTO dto = new TrainerMonthlySummaryDTO(
                "john.doe",
                "John",
                "Doe",
                true,
                List.of(
                        new TrainerMonthlySummaryDTO.YearlySummary(
                                2025,
                                List.of(
                                        new TrainerMonthlySummaryDTO.MonthlySummary(6, 3.5)
                                )
                        )
                )
        );

        Mockito.when(service.getTrainerMonthlySummary("john.doe")).thenReturn(dto);

        mockMvc.perform(get("/api/workload/john.doe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value("john.doe"))
                .andExpect(jsonPath("$.yearlySummaries[0].year").value(2025))
                .andExpect(jsonPath("$.yearlySummaries[0].monthlySummaries[0].totalHours").value(3.5));
    }
}
