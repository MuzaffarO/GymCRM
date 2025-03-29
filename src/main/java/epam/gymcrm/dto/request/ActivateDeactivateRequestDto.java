package epam.gymcrm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivateDeactivateRequestDto {

    @Schema(description = "Username of the user whose status will be changed", example = "john.doe", required = true)
    private String username;

    @Schema(description = "New active status (true for activate, false for deactivate)", example = "true", required = true)
    private Boolean isActive;
}
