package vincent.project_interior.domain.room.dto.response;

import vincent.project_interior.domain.room.entity.FurnitureItem;

public record FurnitureItemResponse(
    String name,
    String positionDescription
) {

  public static FurnitureItemResponse from(FurnitureItem item) {
    return new FurnitureItemResponse(item.getName(), item.getPositionDescription());
  }
}
