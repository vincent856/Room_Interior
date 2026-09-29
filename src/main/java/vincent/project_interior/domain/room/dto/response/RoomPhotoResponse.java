package vincent.project_interior.domain.room.dto.response;

import java.time.Instant;
import java.util.UUID;
import vincent.project_interior.domain.room.entity.RoomPhoto;

public record RoomPhotoResponse(
    UUID id,
    String url,
    String direction,
    Instant createdAt
) {

  public static RoomPhotoResponse from(RoomPhoto photo) {
    return new RoomPhotoResponse(
        photo.getId(),
        "/uploads/" + photo.getStoredPath(),
        photo.getDirection(),
        photo.getCreatedAt()
    );
  }
}
