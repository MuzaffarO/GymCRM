package epam.uz.trainerworkloadservice;

import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.model.MonthlySummary;
import epam.uz.trainerworkloadservice.model.TrainerSummary;
import epam.uz.trainerworkloadservice.repository.MonthlySummaryRepository;
import epam.uz.trainerworkloadservice.repository.TrainerSummaryRepository;
import epam.uz.trainerworkloadservice.service.TrainerWorkloadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    @Mock
    private TrainerSummaryRepository trainerRepo;

    @Mock
    private MonthlySummaryRepository summaryRepo;

    @InjectMocks
    private TrainerWorkloadService workloadService;

    @Test
    void shouldAddWorkloadIfTrainerExists() {
        TrainerSummary trainer = TrainerSummary.builder()
                .trainerUsername("john")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .build();

        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 5, 1))
                .trainingDuration(2.5)
                .actionType(ActionType.ADD)
                .build();

        MonthlySummary existingSummary = MonthlySummary.builder()
                .trainer(trainer)
                .year(2024)
                .month(5)
                .totalHours(1.0)
                .build();

        when(trainerRepo.findByTrainerUsername("john")).thenReturn(Optional.of(trainer));
        when(summaryRepo.findByTrainerAndYearAndMonth(trainer, 2024, 5))
                .thenReturn(Optional.of(existingSummary));

        workloadService.processWorkload(request);

        verify(summaryRepo).save(argThat(summary ->
                summary.getTotalHours() == 3.5 &&
                        summary.getYear() == 2024 &&
                        summary.getMonth() == 5
        ));
    }
}
