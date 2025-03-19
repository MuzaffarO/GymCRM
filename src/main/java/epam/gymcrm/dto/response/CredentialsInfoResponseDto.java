package epam.gymcrm.dto.response;

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
