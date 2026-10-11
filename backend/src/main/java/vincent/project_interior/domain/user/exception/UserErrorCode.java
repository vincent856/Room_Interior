package vincent.project_interior.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import vincent.project_interior.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER-001", "이미 사용 중인 이메일입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-002", "사용자를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
