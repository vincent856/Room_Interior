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
public class PhotoMeasurement extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id", nullable = false)
  private RoomPhoto photo;

  @Column(nullable = false)
  private Double startX;

  @Column(nullable = false)
  private Double startY;

  @Column(nullable = false)
  private Double endX;

  @Column(nullable = false)
  private Double endY;

  @Column(nullable = false)
  private Integer lengthCm;

  private String label;

  private PhotoMeasurement(RoomPhoto photo, Double startX, Double startY, Double endX,
      Double endY, Integer lengthCm, String label) {
    this.photo = Objects.requireNonNull(photo);
    this.startX = Objects.requireNonNull(startX);
    this.startY = Objects.requireNonNull(startY);
    this.endX = Objects.requireNonNull(endX);
    this.endY = Objects.requireNonNull(endY);
    this.lengthCm = Objects.requireNonNull(lengthCm);
    this.label = label;
  }

  public static PhotoMeasurement create(RoomPhoto photo, Double startX, Double startY,
      Double endX, Double endY, Integer lengthCm, String label) {
    PhotoMeasurement measurement = new PhotoMeasurement(photo, startX, startY, endX, endY,
        lengthCm, label);
    photo.addMeasurement(measurement);
    return measurement;
  }
}
