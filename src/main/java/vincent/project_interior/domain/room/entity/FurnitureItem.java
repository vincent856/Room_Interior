package vincent.project_interior.domain.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FurnitureItem {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String positionDescription;

  public FurnitureItem(String name, String positionDescription) {
    this.name = name;
    this.positionDescription = positionDescription;
  }
}
