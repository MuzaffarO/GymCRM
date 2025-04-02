package epam.gymcrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
