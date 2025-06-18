package epam.uz.trainerworkloadservice;

import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.model.TrainerTrainingSummary;
import epam.uz.trainerworkloadservice.repository.TrainerTrainingSummaryRepository;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerMongoWorkloadServiceTest {

    @Mock
    private TrainerTrainingSummaryRepository repository;

    @InjectMocks
    private TrainerMongoWorkloadService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNewTrainerDocumentIfNotExists() {
        // given
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john.doe")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2025, 6, 1))
                .trainingDuration(2.5)
                .actionType(ActionType.ADD)
                .build();

        when(repository.findByTrainerUsername("john.doe")).thenReturn(Optional.empty());

        // when
        service.processWorkload(request);

        // then
        ArgumentCaptor<TrainerTrainingSummary> captor = ArgumentCaptor.forClass(TrainerTrainingSummary.class);
        verify(repository).save(captor.capture());

        TrainerTrainingSummary saved = captor.getValue();
        assertEquals("john.doe", saved.getTrainerUsername());
        assertEquals("John", saved.getFirstName());
        assertEquals(2025, saved.getYearlySummaries().get(0).getYear());
        assertEquals(6, saved.getYearlySummaries().get(0).getMonthlySummaries().get(0).getMonth());
        assertEquals(2.5, saved.getYearlySummaries().get(0).getMonthlySummaries().get(0).getTrainingsSummaryDuration());
    }

    @Test
    void shouldUpdateExistingMonthlySummary() {
        // existing trainer with June 2025 data
        TrainerTrainingSummary.MonthlySummary existingMonth = TrainerTrainingSummary.MonthlySummary.builder()
                .month(6).trainingsSummaryDuration(1.5).build();

        TrainerTrainingSummary.YearlySummary existingYear = TrainerTrainingSummary.YearlySummary.builder()
                .year(2025)
                .monthlySummaries(new ArrayList<>(List.of(existingMonth)))
                .build();

        TrainerTrainingSummary existingTrainer = TrainerTrainingSummary.builder()
                .trainerUsername("john.doe")
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .yearlySummaries(new ArrayList<>(List.of(existingYear)))
                .build();

        when(repository.findByTrainerUsername("john.doe")).thenReturn(Optional.of(existingTrainer));

        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john.doe")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2025, 6, 1))
                .trainingDuration(1.5)
                .actionType(ActionType.ADD)
                .build();

        service.processWorkload(request);

        verify(repository).save(existingTrainer);
        assertEquals(3.0, existingMonth.getTrainingsSummaryDuration());
    }

    @Test
    void shouldSubtractTrainingOnDelete() {
        TrainerTrainingSummary.MonthlySummary month = TrainerTrainingSummary.MonthlySummary.builder()
                .month(3).trainingsSummaryDuration(10.0).build();

        TrainerTrainingSummary.YearlySummary year = TrainerTrainingSummary.YearlySummary.builder()
                .year(2024)
                .monthlySummaries(List.of(month)).build();

        TrainerTrainingSummary trainer = TrainerTrainingSummary.builder()
                .trainerUsername("trainer.delete")
                .firstName("Del")
                .lastName("Tester")
                .active(true)
                .yearlySummaries(List.of(year))
                .build();

        when(repository.findByTrainerUsername("trainer.delete")).thenReturn(Optional.of(trainer));

        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("trainer.delete")
                .trainerFirstName("Del")
                .trainerLastName("Tester")
                .isActive(true)
                .trainingDate(LocalDate.of(2024, 3, 1))
                .trainingDuration(3.0)
                .actionType(ActionType.DELETE)
                .build();

        service.processWorkload(request);

        verify(repository).save(trainer);
        assertEquals(7.0, month.getTrainingsSummaryDuration());
    }

    @Test
    void shouldReturnCorrectMonthlyHours() {
        TrainerTrainingSummary.MonthlySummary month = TrainerTrainingSummary.MonthlySummary.builder()
                .month(1).trainingsSummaryDuration(5.5).build();

        TrainerTrainingSummary.YearlySummary year = TrainerTrainingSummary.YearlySummary.builder()
                .year(2023)
                .monthlySummaries(List.of(month)).build();

        TrainerTrainingSummary trainer = TrainerTrainingSummary.builder()
                .trainerUsername("summary.test")
                .firstName("Sum")
                .lastName("Tester")
                .active(true)
                .yearlySummaries(List.of(year))
                .build();

        when(repository.findByTrainerUsername("summary.test")).thenReturn(Optional.of(trainer));

        double result = service.getMonthlyHours("summary.test", 2023, 1);

        assertEquals(5.5, result);
    }

    @Test
    void shouldThrowIfMonthNotFound() {
        TrainerTrainingSummary trainer = TrainerTrainingSummary.builder()
                .trainerUsername("missing.month")
                .yearlySummaries(List.of())
                .build();

        when(repository.findByTrainerUsername("missing.month")).thenReturn(Optional.of(trainer));

        Exception ex = assertThrows(RuntimeException.class, () ->
                service.getMonthlyHours("missing.month", 2022, 2));

        assertTrue(ex.getMessage().contains("No summary found"));
    }
}
