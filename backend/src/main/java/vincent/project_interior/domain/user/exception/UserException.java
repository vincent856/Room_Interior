package vincent.project_interior.domain.user.exception;

import java.util.Map;
import vincent.project_interior.global.error.CustomException;
import vincent.project_interior.global.error.ErrorCode;

public class UserException extends CustomException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
