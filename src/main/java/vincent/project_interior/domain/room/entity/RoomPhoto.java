package vincent.project_interior.domain.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vincent.project_interior.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomPhoto extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(nullable = false)
  private String storedPath;

  private String direction;

  private RoomPhoto(Room room, String storedPath, String direction) {
    this.room = Objects.requireNonNull(room);
    this.storedPath = Objects.requireNonNull(storedPath);
    this.direction = direction;
  }

  public static RoomPhoto create(Room room, String storedPath, String direction) {
    RoomPhoto photo = new RoomPhoto(room, storedPath, direction);
    room.addPhoto(photo);
    return photo;
  }
}
