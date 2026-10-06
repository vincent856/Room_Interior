package vincent.project_interior.global.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vincent.project_interior.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.warn("[CustomException] Type: {}, Code: {}, Message: {}, Details: {}",
        e.getClass().getSimpleName(), errorCode.getCode(), e.getMessage(), e.getDetails());
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage(), e.getDetails()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException e) {
    log.warn("[TypeMismatch] Parameter: {}, ExpectedType: {}", e.getName(), e.getRequiredType());
    return ResponseEntity
        .status(CommonErrorCode.INVALID_INPUT.getStatus())
        .body(ApiResponse.error(CommonErrorCode.INVALID_INPUT.getCode(),
            CommonErrorCode.INVALID_INPUT.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .orElse(CommonErrorCode.INVALID_INPUT.getMessage());
    log.warn("[Validation failed] Message: {}", message);
    return ResponseEntity
        .status(CommonErrorCode.INVALID_INPUT.getStatus())
        .body(ApiResponse.error(CommonErrorCode.INVALID_INPUT.getCode(), message));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
      NoResourceFoundException e) {
    log.warn("[NoResourceFound] URL: {}", e.getResourcePath());
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error("NOT_FOUND", "요청한 경로를 찾을 수 없습니다."));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(
      NoHandlerFoundException e) {
    log.warn("[NoHandlerFound] URL: {}", e.getRequestURL());
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error("NOT_FOUND", "요청한 경로를 찾을 수 없습니다."));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("[Unhandled exception] Message: {}", e.getMessage(), e);
    CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
  }
}
