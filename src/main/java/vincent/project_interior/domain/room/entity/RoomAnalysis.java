package vincent.project_interior.domain.room.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class RoomAnalysis extends BaseTimeEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(nullable = false)
  private String roomType;

  @Column(nullable = false)
  private String style;

  @Column(nullable = false)
  private String wallColor;

  @Column(nullable = false)
  private String floorMaterial;

  @Column(nullable = false)
  private String lightingDescription;

  @Column(nullable = false)
  private String layoutSummary;

  @ElementCollection
  @CollectionTable(name = "room_analysis_furniture_item", joinColumns = @JoinColumn(name = "room_analysis_id"))
  private final List<FurnitureItem> existingFurniture = new ArrayList<>();

  @Lob
  @Column(nullable = false)
  private String rawModelResponse;

  private RoomAnalysis(Room room, String roomType, String style, String wallColor,
      String floorMaterial, String lightingDescription, String layoutSummary,
      List<FurnitureItem> existingFurniture, String rawModelResponse) {
    this.room = Objects.requireNonNull(room);
    this.roomType = Objects.requireNonNull(roomType);
    this.style = Objects.requireNonNull(style);
    this.wallColor = Objects.requireNonNull(wallColor);
    this.floorMaterial = Objects.requireNonNull(floorMaterial);
    this.lightingDescription = Objects.requireNonNull(lightingDescription);
    this.layoutSummary = Objects.requireNonNull(layoutSummary);
    this.existingFurniture.addAll(existingFurniture);
    this.rawModelResponse = Objects.requireNonNull(rawModelResponse);
  }

  public static RoomAnalysis create(Room room, String roomType, String style, String wallColor,
      String floorMaterial, String lightingDescription, String layoutSummary,
      List<FurnitureItem> existingFurniture, String rawModelResponse) {
    RoomAnalysis analysis = new RoomAnalysis(room, roomType, style, wallColor, floorMaterial,
        lightingDescription, layoutSummary, existingFurniture, rawModelResponse);
    room.addAnalysis(analysis);
    return analysis;
  }
}
