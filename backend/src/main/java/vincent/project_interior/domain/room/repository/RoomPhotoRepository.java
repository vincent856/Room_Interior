package vincent.project_interior.domain.room.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vincent.project_interior.domain.room.entity.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, UUID> {

}
