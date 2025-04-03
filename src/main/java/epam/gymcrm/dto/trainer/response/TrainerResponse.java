package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.trainer.request.SpecializationName;

public record TrainerResponse(
        String username,
        String firstName,
        String lastName,
        SpecializationName specialization

) { }