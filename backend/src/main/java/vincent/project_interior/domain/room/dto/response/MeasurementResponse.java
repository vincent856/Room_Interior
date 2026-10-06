package vincent.project_interior.domain.room.dto.response;

import java.util.UUID;
import vincent.project_interior.domain.room.entity.PhotoMeasurement;

public record MeasurementResponse(
    UUID id,
    Double startX,
    Double startY,
    Double endX,
    Double endY,
    Integer lengthCm,
    String label
) {

  public static MeasurementResponse from(PhotoMeasurement measurement) {
    return new MeasurementResponse(
        measurement.getId(),
        measurement.getStartX(),
        measurement.getStartY(),
        measurement.getEndX(),
        measurement.getEndY(),
        measurement.getLengthCm(),
        measurement.getLabel()
    );
  }
}
