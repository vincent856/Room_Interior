package vincent.project_interior.domain.room.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import vincent.project_interior.domain.room.entity.RoomAnalysis;

public record RoomAnalysisResponse(
    UUID id,
    String roomType,
    String style,
    String wallColor,
    String floorMaterial,
    String lightingDescription,
    String layoutSummary,
    List<FurnitureItemResponse> existingFurniture,
    Instant createdAt
) {

  public static RoomAnalysisResponse from(RoomAnalysis analysis) {
    return new RoomAnalysisResponse(
        analysis.getId(),
        analysis.getRoomType(),
        analysis.getStyle(),
        analysis.getWallColor(),
        analysis.getFloorMaterial(),
        analysis.getLightingDescription(),
        analysis.getLayoutSummary(),
        analysis.getExistingFurniture().stream().map(FurnitureItemResponse::from).toList(),
        analysis.getCreatedAt()
    );
  }
}
