package vincent.project_interior.domain.room.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MeasurementsUpdateRequest(
    @NotNull @Valid List<MeasurementRequest> measurements
) {

}
