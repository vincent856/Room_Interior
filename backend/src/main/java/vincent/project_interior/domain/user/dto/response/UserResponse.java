package vincent.project_interior.domain.user.dto.response;

import java.time.Instant;
import java.util.UUID;
import vincent.project_interior.domain.user.entity.User;

public record UserResponse(
    UUID id,
    String email,
    String nickname,
    Instant createdAt
) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), user.getCreatedAt());
  }
}
