package vincent.project_interior.infra.externalapi.gemini.exception;

public class GeminiClientException extends RuntimeException {

  public GeminiClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
