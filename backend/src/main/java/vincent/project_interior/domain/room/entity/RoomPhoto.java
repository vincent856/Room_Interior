package vincent.project_interior.domain.room.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<PhotoMeasurement> measurements = new ArrayList<>();

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

  public void addMeasurement(PhotoMeasurement measurement) {
    measurements.add(measurement);
  }

  public void clearMeasurements() {
    measurements.clear();
  }
}
