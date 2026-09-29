package vincent.project_interior.domain.room.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import vincent.project_interior.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode {

  ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM-001", "방을 찾을 수 없습니다."),
  INVALID_PHOTO_FILE(HttpStatus.BAD_REQUEST, "ROOM-002", "이미지 파일(jpg, jpeg, png, webp)만 업로드할 수 있습니다."),
  NO_PHOTOS_FOR_ANALYSIS(HttpStatus.BAD_REQUEST, "ROOM-003", "분석할 사진이 없습니다."),
  ANALYSIS_FAILED(HttpStatus.BAD_GATEWAY, "ROOM-004", "방 구조 분석에 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
