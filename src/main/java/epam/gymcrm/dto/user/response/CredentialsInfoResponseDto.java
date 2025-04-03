package epam.gymcrm.dto.user.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CredentialsInfoResponseDto {
    private String username;
    private String password;
}
