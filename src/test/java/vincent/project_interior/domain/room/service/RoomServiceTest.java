package vincent.project_interior.domain.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import vincent.project_interior.domain.room.dto.request.RoomCreateRequest;
import vincent.project_interior.domain.room.dto.response.RoomAnalysisResponse;
import vincent.project_interior.domain.room.dto.response.RoomPhotoResponse;
import vincent.project_interior.domain.room.dto.response.RoomResponse;
import vincent.project_interior.domain.room.exception.RoomErrorCode;
import vincent.project_interior.domain.room.exception.RoomException;
import vincent.project_interior.infra.externalapi.gemini.GeminiVisionClient;
import vincent.project_interior.infra.externalapi.gemini.dto.FurnitureItemResult;
import vincent.project_interior.infra.externalapi.gemini.dto.RoomVisionAnalysisResult;
import vincent.project_interior.infra.externalapi.gemini.exception.GeminiClientException;

@SpringBootTest
@Transactional
class RoomServiceTest {

  @Autowired
  private RoomService roomService;

  @MockitoBean
  private GeminiVisionClient geminiVisionClient;

  @Test
  void 방을_등록하면_저장한_치수가_그대로_조회된다() {
    RoomResponse created = roomService.createRoom(
        new RoomCreateRequest("안방", 350, 400, 230));

    RoomResponse found = roomService.getRoom(created.id());

    assertThat(found.name()).isEqualTo("안방");
    assertThat(found.widthCm()).isEqualTo(350);
    assertThat(found.lengthCm()).isEqualTo(400);
    assertThat(found.heightCm()).isEqualTo(230);
    assertThat(found.photos()).isEmpty();
  }

  @Test
  void 존재하지_않는_방을_조회하면_ROOM_NOT_FOUND_예외가_발생한다() {
    UUID missingId = UUID.randomUUID();

    assertThatThrownBy(() -> roomService.getRoom(missingId))
        .isInstanceOf(RoomException.class)
        .extracting(e -> ((RoomException) e).getErrorCode())
        .isEqualTo(RoomErrorCode.ROOM_NOT_FOUND);
  }

  @Test
  void 이미지가_아닌_파일을_업로드하면_INVALID_PHOTO_FILE_예외가_발생한다() {
    RoomResponse room = roomService.createRoom(
        new RoomCreateRequest("거실", 500, 600, 240));
    MockMultipartFile textFile = new MockMultipartFile(
        "file", "note.txt", "text/plain", "not an image".getBytes());

    assertThatThrownBy(() -> roomService.addPhoto(room.id(), textFile, "NORTH"))
        .isInstanceOf(RoomException.class)
        .extracting(e -> ((RoomException) e).getErrorCode())
        .isEqualTo(RoomErrorCode.INVALID_PHOTO_FILE);

    RoomResponse found = roomService.getRoom(room.id());
    assertThat(found.photos()).isEmpty();
  }

  @Test
  void 이미지_파일을_업로드하면_방에_사진이_추가된다() {
    RoomResponse room = roomService.createRoom(
        new RoomCreateRequest("주방", 300, 350, 230));
    MockMultipartFile imageFile = new MockMultipartFile(
        "file", "wall.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

    RoomPhotoResponse photo = roomService.addPhoto(room.id(), imageFile, "NORTH");

    assertThat(photo.direction()).isEqualTo("NORTH");
    assertThat(photo.url()).startsWith("/uploads/");

    RoomResponse found = roomService.getRoom(room.id());
    assertThat(found.photos()).hasSize(1);
    assertThat(found.photos().get(0).url()).isEqualTo(photo.url());
  }

  @Test
  void 사진이_있는_방을_분석하면_분석_결과가_저장되고_방_조회시_함께_반환된다() {
    RoomResponse room = roomService.createRoom(
        new RoomCreateRequest("거실", 500, 600, 240));
    MockMultipartFile imageFile = new MockMultipartFile(
        "file", "wall.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});
    roomService.addPhoto(room.id(), imageFile, "NORTH");

    RoomVisionAnalysisResult fakeResult = new RoomVisionAnalysisResult(
        "거실", "모던", "화이트", "우드 플로어링", "자연광이 잘 드는 남향",
        "소파와 TV장이 마주보는 구조",
        List.of(new FurnitureItemResult("소파", "북쪽 벽면에 붙어 있음")),
        "{}");
    when(geminiVisionClient.analyze(any())).thenReturn(fakeResult);

    RoomAnalysisResponse analysis = roomService.analyzeRoom(room.id());

    assertThat(analysis.roomType()).isEqualTo("거실");
    assertThat(analysis.style()).isEqualTo("모던");
    assertThat(analysis.existingFurniture()).hasSize(1);
    assertThat(analysis.existingFurniture().get(0).name()).isEqualTo("소파");

    RoomResponse found = roomService.getRoom(room.id());
    assertThat(found.latestAnalysis()).isNotNull();
    assertThat(found.latestAnalysis().roomType()).isEqualTo("거실");
  }

  @Test
  void 사진이_없는_방을_분석하면_NO_PHOTOS_FOR_ANALYSIS_예외가_발생한다() {
    RoomResponse room = roomService.createRoom(
        new RoomCreateRequest("서재", 300, 300, 230));

    assertThatThrownBy(() -> roomService.analyzeRoom(room.id()))
        .isInstanceOf(RoomException.class)
        .extracting(e -> ((RoomException) e).getErrorCode())
        .isEqualTo(RoomErrorCode.NO_PHOTOS_FOR_ANALYSIS);
  }

  @Test
  void OpenAI_호출이_실패하면_ANALYSIS_FAILED_예외가_발생한다() {
    RoomResponse room = roomService.createRoom(
        new RoomCreateRequest("침실", 350, 400, 230));
    MockMultipartFile imageFile = new MockMultipartFile(
        "file", "wall.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});
    roomService.addPhoto(room.id(), imageFile, "SOUTH");

    when(geminiVisionClient.analyze(any()))
        .thenThrow(new GeminiClientException("실패", new RuntimeException()));

    assertThatThrownBy(() -> roomService.analyzeRoom(room.id()))
        .isInstanceOf(RoomException.class)
        .extracting(e -> ((RoomException) e).getErrorCode())
        .isEqualTo(RoomErrorCode.ANALYSIS_FAILED);
  }
}
