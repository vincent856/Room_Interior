package vincent.project_interior.infra.externalapi.gemini.dto;

import java.util.List;

public record PhotoInput(
    byte[] imageBytes,
    String contentType,
    String direction,
    List<MeasurementInput> measurements
) {

}
