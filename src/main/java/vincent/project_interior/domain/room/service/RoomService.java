package vincent.project_interior.domain.room.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vincent.project_interior.domain.room.dto.request.RoomCreateRequest;
import vincent.project_interior.domain.room.dto.response.RoomAnalysisResponse;
import vincent.project_interior.domain.room.dto.response.RoomPhotoResponse;
import vincent.project_interior.domain.room.dto.response.RoomResponse;
import vincent.project_interior.domain.room.entity.FurnitureItem;
import vincent.project_interior.domain.room.entity.Room;
import vincent.project_interior.domain.room.entity.RoomAnalysis;
import vincent.project_interior.domain.room.entity.RoomPhoto;
import vincent.project_interior.domain.room.exception.RoomErrorCode;
import vincent.project_interior.domain.room.exception.RoomException;
import vincent.project_interior.domain.room.repository.RoomRepository;
import vincent.project_interior.global.storage.FileStorageService;
import vincent.project_interior.infra.externalapi.gemini.GeminiVisionClient;
import vincent.project_interior.infra.externalapi.gemini.dto.PhotoInput;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisRequest;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisResult;
import vincent.project_interior.infra.externalapi.gemini.exception.GeminiClientException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

  private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");
  private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = Map.of(
      ".jpg", "image/jpeg",
      ".jpeg", "image/jpeg",
      ".png", "image/png",
      ".webp", "image/webp"
  );

  private final RoomRepository roomRepository;
  private final FileStorageService fileStorageService;
  private final GeminiVisionClient geminiVisionClient;

  @Transactional
  public RoomResponse createRoom(RoomCreateRequest request) {
    log.debug("방 등록 시도: name={}, widthCm={}, lengthCm={}, heightCm={}",
        request.name(), request.widthCm(), request.lengthCm(), request.heightCm());
    Room room = Room.create(request.name(), request.widthCm(), request.lengthCm(),
        request.heightCm());
    Room saved = roomRepository.save(room);
    log.info("방 등록 완료: roomId={}", saved.getId());
    return RoomResponse.from(saved);
  }

  @Transactional
  public RoomPhotoResponse addPhoto(UUID roomId, MultipartFile file, String direction) {
    log.debug("방 사진 업로드 시도: roomId={}, direction={}", roomId, direction);
    Room room = findRoom(roomId);
    validateImageFile(file);
    String storedPath = fileStorageService.store(file);
    RoomPhoto photo = RoomPhoto.create(room, storedPath, direction);
    roomRepository.flush();
    log.info("방 사진 업로드 완료: roomId={}, storedPath={}", roomId, storedPath);
    return RoomPhotoResponse.from(photo);
  }

  public RoomResponse getRoom(UUID roomId) {
    return RoomResponse.from(findRoom(roomId));
  }

  @Transactional
  public RoomAnalysisResponse analyzeRoom(UUID roomId) {
    log.debug("방 구조 분석 시도: roomId={}", roomId);
    Room room = findRoom(roomId);
    if (room.getPhotos().isEmpty()) {
      throw new RoomException(RoomErrorCode.NO_PHOTOS_FOR_ANALYSIS);
    }

    List<PhotoInput> photos = room.getPhotos().stream()
        .map(photo -> new PhotoInput(
            fileStorageService.load(photo.getStoredPath()),
            resolveContentType(photo.getStoredPath()),
            photo.getDirection()))
        .toList();
    RoomVisionAnalysisRequest request = new RoomVisionAnalysisRequest(
        photos, room.getWidthCm(), room.getLengthCm(), room.getHeightCm());

    RoomVisionAnalysisResult result;
    try {
      result = geminiVisionClient.analyze(request);
    } catch (GeminiClientException e) {
      log.error("방 구조 분석 실패: roomId={}", roomId, e);
      throw new RoomException(RoomErrorCode.ANALYSIS_FAILED);
    }

    List<FurnitureItem> furniture = result.existingFurniture().stream()
        .map(item -> new FurnitureItem(item.name(), item.positionDescription()))
        .toList();
    RoomAnalysis analysis = RoomAnalysis.create(room, result.roomType(), result.style(),
        result.wallColor(), result.floorMaterial(), result.lightingDescription(),
        result.layoutSummary(), furniture, result.rawJson());
    roomRepository.flush();
    log.info("방 구조 분석 완료: roomId={}", roomId);
    return RoomAnalysisResponse.from(analysis);
  }

  private String resolveContentType(String storedPath) {
    String lower = storedPath.toLowerCase(Locale.ROOT);
    return EXTENSION_TO_CONTENT_TYPE.entrySet().stream()
        .filter(entry -> lower.endsWith(entry.getKey()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse("application/octet-stream");
  }

  private Room findRoom(UUID roomId) {
    return roomRepository.findById(roomId)
        .orElseThrow(() -> new RoomException(RoomErrorCode.ROOM_NOT_FOUND));
  }

  private void validateImageFile(MultipartFile file) {
    String filename = file.getOriginalFilename();
    boolean allowed = filename != null && ALLOWED_EXTENSIONS.stream()
        .anyMatch(ext -> filename.toLowerCase(Locale.ROOT).endsWith(ext));
    if (!allowed) {
      throw new RoomException(RoomErrorCode.INVALID_PHOTO_FILE);
    }
  }
}
