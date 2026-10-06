package vincent.project_interior.domain.room.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vincent.project_interior.domain.room.dto.request.MeasurementsUpdateRequest;
import vincent.project_interior.domain.room.dto.request.RoomCreateRequest;
import vincent.project_interior.domain.room.dto.response.MeasurementResponse;
import vincent.project_interior.domain.room.dto.response.RoomAnalysisResponse;
import vincent.project_interior.domain.room.dto.response.RoomPhotoResponse;
import vincent.project_interior.domain.room.dto.response.RoomResponse;
import vincent.project_interior.domain.room.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping
  public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(request));
  }

  @PostMapping("/{roomId}/photos")
  public ResponseEntity<RoomPhotoResponse> addPhoto(
      @PathVariable UUID roomId,
      @RequestPart MultipartFile file,
      @RequestParam(required = false) String direction) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(roomService.addPhoto(roomId, file, direction));
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<RoomResponse> getRoom(@PathVariable UUID roomId) {
    return ResponseEntity.ok(roomService.getRoom(roomId));
  }

  @PutMapping("/{roomId}/photos/{photoId}/measurements")
  public ResponseEntity<List<MeasurementResponse>> updatePhotoMeasurements(
      @PathVariable UUID roomId,
      @PathVariable UUID photoId,
      @Valid @RequestBody MeasurementsUpdateRequest request) {
    return ResponseEntity.ok(roomService.updatePhotoMeasurements(roomId, photoId, request));
  }

  @PostMapping("/{roomId}/analyze")
  public ResponseEntity<RoomAnalysisResponse> analyzeRoom(@PathVariable UUID roomId) {
    return ResponseEntity.status(HttpStatus.CREATED).body(roomService.analyzeRoom(roomId));
  }
}
