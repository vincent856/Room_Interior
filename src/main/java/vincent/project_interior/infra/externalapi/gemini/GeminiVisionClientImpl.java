package vincent.project_interior.infra.externalapi.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import vincent.project_interior.infra.externalapi.gemini.dto.FurnitureItemResult;
import vincent.project_interior.infra.externalapi.gemini.dto.PhotoInput;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisRequest;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisResult;
import vincent.project_interior.infra.externalapi.gemini.exception.GeminiClientException;

@Slf4j
@Component
public class GeminiVisionClientImpl implements GeminiVisionClient {

  private static final String SYSTEM_PROMPT = """
      당신은 인테리어 전문가입니다. 사용자가 제공하는 방 사진과 실측 치수를 분석해서
      방의 구조와 현재 상태를 한국어로 정확하고 간결하게 설명해주세요.
      """;

  private static final String RESPONSE_JSON_SCHEMA = """
      {
        "type": "OBJECT",
        "properties": {
          "roomType": {"type": "STRING"},
          "style": {"type": "STRING"},
          "wallColor": {"type": "STRING"},
          "floorMaterial": {"type": "STRING"},
          "lightingDescription": {"type": "STRING"},
          "layoutSummary": {"type": "STRING"},
          "existingFurniture": {
            "type": "ARRAY",
            "items": {
              "type": "OBJECT",
              "properties": {
                "name": {"type": "STRING"},
                "positionDescription": {"type": "STRING"}
              },
              "required": ["name", "positionDescription"]
            }
          }
        },
        "required": ["roomType", "style", "wallColor", "floorMaterial", "lightingDescription", "layoutSummary", "existingFurniture"]
      }
      """;

  private final RestClient geminiRestClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${gemini.model}")
  private String model;

  public GeminiVisionClientImpl(RestClient geminiRestClient) {
    this.geminiRestClient = geminiRestClient;
  }

  @Override
  public RoomVisionAnalysisResult analyze(RoomVisionAnalysisRequest request) {
    try {
      Map<String, Object> requestBody = buildRequestBody(request);
      String rawResponse = geminiRestClient.post()
          .uri("/models/{model}:generateContent", model)
          .contentType(MediaType.APPLICATION_JSON)
          .body(requestBody)
          .retrieve()
          .body(String.class);
      return parseResponse(rawResponse);
    } catch (RestClientException | JsonProcessingException e) {
      log.error("Gemini Vision API 호출 실패", e);
      throw new GeminiClientException("Gemini Vision API 호출에 실패했습니다.", e);
    }
  }

  private Map<String, Object> buildRequestBody(RoomVisionAnalysisRequest request)
      throws JsonProcessingException {
    List<Map<String, Object>> parts = new ArrayList<>();
    parts.add(Map.of("text", buildUserPrompt(request)));
    for (PhotoInput photo : request.photos()) {
      String base64 = Base64.getEncoder().encodeToString(photo.imageBytes());
      parts.add(Map.of(
          "inlineData", Map.of(
              "mimeType", photo.contentType(),
              "data", base64
          )
      ));
    }

    Map<String, Object> schema = objectMapper.readValue(
        RESPONSE_JSON_SCHEMA, new TypeReference<Map<String, Object>>() {
        });

    return Map.of(
        "systemInstruction", Map.of("parts", List.of(Map.of("text", SYSTEM_PROMPT))),
        "contents", List.of(Map.of("role", "user", "parts", parts)),
        "generationConfig", Map.of(
            "responseMimeType", "application/json",
            "responseSchema", schema
        )
    );
  }

  private String buildUserPrompt(RoomVisionAnalysisRequest request) {
    StringBuilder prompt = new StringBuilder();
    prompt.append("이 방의 실측 치수는 가로 ").append(request.widthCm())
        .append("cm, 세로 ").append(request.lengthCm())
        .append("cm, 높이 ").append(request.heightCm())
        .append("cm 입니다. 첨부된 사진들을 보고 방의 구조와 현재 상태를 분석해주세요.");
    for (PhotoInput photo : request.photos()) {
      if (photo.direction() != null) {
        prompt.append(" 다음 사진의 촬영 방향: ").append(photo.direction()).append(".");
      }
    }
    return prompt.toString();
  }

  private RoomVisionAnalysisResult parseResponse(String rawResponse)
      throws JsonProcessingException {
    JsonNode root = objectMapper.readTree(rawResponse);
    String content = root.path("candidates").path(0).path("content")
        .path("parts").path(0).path("text").asText();
    ParsedAnalysis parsed = objectMapper.readValue(content, ParsedAnalysis.class);

    List<FurnitureItemResult> furniture = parsed.existingFurniture() == null
        ? List.of()
        : parsed.existingFurniture().stream()
            .map(item -> new FurnitureItemResult(item.name(), item.positionDescription()))
            .toList();

    return new RoomVisionAnalysisResult(
        parsed.roomType(),
        parsed.style(),
        parsed.wallColor(),
        parsed.floorMaterial(),
        parsed.lightingDescription(),
        parsed.layoutSummary(),
        furniture,
        content
    );
  }

  private record ParsedAnalysis(
      String roomType,
      String style,
      String wallColor,
      String floorMaterial,
      String lightingDescription,
      String layoutSummary,
      List<ParsedFurnitureItem> existingFurniture
  ) {

  }

  private record ParsedFurnitureItem(
      String name,
      String positionDescription
  ) {

  }
}
