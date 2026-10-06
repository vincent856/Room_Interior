package vincent.project_interior.domain.room.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vincent.project_interior.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer widthCm;

  @Column(nullable = false)
  private Integer lengthCm;

  @Column(nullable = false)
  private Integer heightCm;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<RoomPhoto> photos = new ArrayList<>();

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<RoomAnalysis> analyses = new ArrayList<>();

  private Room(String name, Integer widthCm, Integer lengthCm, Integer heightCm) {
    this.name = Objects.requireNonNull(name);
    this.widthCm = Objects.requireNonNull(widthCm);
    this.lengthCm = Objects.requireNonNull(lengthCm);
    this.heightCm = Objects.requireNonNull(heightCm);
  }

  public static Room create(String name, Integer widthCm, Integer lengthCm, Integer heightCm) {
    return new Room(name, widthCm, lengthCm, heightCm);
  }

  public void addPhoto(RoomPhoto photo) {
    photos.add(photo);
  }

  public void addAnalysis(RoomAnalysis analysis) {
    analyses.add(analysis);
  }

  public RoomAnalysis getLatestAnalysis() {
    return analyses.isEmpty() ? null : analyses.get(analyses.size() - 1);
  }
}
