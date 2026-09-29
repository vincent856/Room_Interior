package vincent.project_interior.infra.externalapi.gemini;

import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisRequest;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisResult;

public interface GeminiVisionClient {

  RoomVisionAnalysisResult analyze(RoomVisionAnalysisRequest request);
}
