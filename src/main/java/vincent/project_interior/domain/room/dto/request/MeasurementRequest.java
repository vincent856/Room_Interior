package vincent.project_interior.domain.room.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MeasurementRequest(
    @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double startX,
    @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double startY,
    @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double endX,
    @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double endY,
    @NotNull @Positive Integer lengthCm,
    String label
) {

}
