package vincent.project_interior.infra.externalapi.gemini.dto;

import java.util.List;

public record RoomVisionAnalysisResult(
    String roomType,
    String style,
    String wallColor,
    String floorMaterial,
    String lightingDescription,
    String layoutSummary,
    List<FurnitureItemResult> existingFurniture,
    String rawJson
) {

}
