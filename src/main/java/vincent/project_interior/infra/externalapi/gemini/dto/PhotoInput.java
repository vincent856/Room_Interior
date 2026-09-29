package vincent.project_interior.infra.externalapi.gemini.dto;

public record PhotoInput(
    byte[] imageBytes,
    String contentType,
    String direction
) {

}
