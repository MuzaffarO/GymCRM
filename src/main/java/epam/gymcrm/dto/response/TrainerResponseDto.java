package epam.gymcrm.dto.response;

public record TrainerResponseDto(
        String username,
        String firstName,
        String lastName,
        TrainingTypeResponseDto specialization

) { }