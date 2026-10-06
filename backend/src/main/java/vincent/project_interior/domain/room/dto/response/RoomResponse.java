package vincent.project_interior.domain.room.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import vincent.project_interior.domain.room.entity.Room;
import vincent.project_interior.domain.room.entity.RoomAnalysis;

public record RoomResponse(
    UUID id,
    String name,
    Integer widthCm,
    Integer lengthCm,
    Integer heightCm,
    Instant createdAt,
    List<RoomPhotoResponse> photos,
    RoomAnalysisResponse latestAnalysis
) {

  public static RoomResponse from(Room room) {
    RoomAnalysis latestAnalysis = room.getLatestAnalysis();
    return new RoomResponse(
        room.getId(),
        room.getName(),
        room.getWidthCm(),
        room.getLengthCm(),
        room.getHeightCm(),
        room.getCreatedAt(),
        room.getPhotos().stream().map(RoomPhotoResponse::from).toList(),
        latestAnalysis == null ? null : RoomAnalysisResponse.from(latestAnalysis)
    );
  }
}
