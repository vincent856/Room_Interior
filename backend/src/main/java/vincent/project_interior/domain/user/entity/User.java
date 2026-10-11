package vincent.project_interior.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vincent.project_interior.global.entity.BaseTimeEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private boolean locked;

  private User(String email, String password, String nickname) {
    this.email = Objects.requireNonNull(email);
    this.password = Objects.requireNonNull(password);
    this.nickname = Objects.requireNonNull(nickname);
    this.locked = false;
  }

  public static User createUser(String email, String encodedPassword, String nickname) {
    return new User(email, encodedPassword, nickname);
  }
}
