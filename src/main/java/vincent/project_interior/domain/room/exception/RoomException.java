package vincent.project_interior.domain.room.exception;

import java.util.Map;
import vincent.project_interior.global.error.CustomException;
import vincent.project_interior.global.error.ErrorCode;

public class RoomException extends CustomException {

  public RoomException(ErrorCode errorCode) {
    super(errorCode);
  }

  public RoomException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
