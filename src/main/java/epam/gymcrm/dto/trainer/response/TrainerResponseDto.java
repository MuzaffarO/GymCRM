package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.trainer.request.SpecializationNameDto;

public record TrainerResponseDto(
        String username,
        String firstName,
        String lastName,
        SpecializationNameDto specialization

) { }