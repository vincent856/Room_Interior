package vincent.project_interior.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, max = 20) String password,
    @NotBlank @Size(min = 2, max = 15) String nickname
) {

}
