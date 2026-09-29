package vincent.project_interior.domain.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomCreateRequest(
    @NotBlank String name,
    @NotNull @Positive Integer widthCm,
    @NotNull @Positive Integer lengthCm,
    @NotNull @Positive Integer heightCm
) {

}
