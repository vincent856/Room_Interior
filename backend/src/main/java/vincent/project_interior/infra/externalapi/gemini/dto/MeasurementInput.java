package vincent.project_interior.infra.externalapi.gemini.dto;

public record MeasurementInput(
    Double startX,
    Double startY,
    Double endX,
    Double endY,
    Integer lengthCm,
    String label
) {

}
