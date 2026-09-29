package vincent.project_interior.infra.externalapi.gemini.dto;

import java.util.List;

public record RoomVisionAnalysisRequest(
    List<PhotoInput> photos,
    Integer widthCm,
    Integer lengthCm,
    Integer heightCm
) {

}
