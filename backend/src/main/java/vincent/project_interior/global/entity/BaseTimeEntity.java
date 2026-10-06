package vincent.project_interior.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
public abstract class BaseTimeEntity extends BaseEntity {

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;
}
