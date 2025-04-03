package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.user.request.SpecializationName;

public record TrainerResponse(
        String username,
        String firstName,
        String lastName,
        SpecializationName specialization

) { }