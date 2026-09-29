package vincent.project_interior.global.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  // 반환값은 /uploads/** 리소스 핸들러로 접근 가능한 저장 파일명
  String store(MultipartFile file);

  byte[] load(String storedPath);
}
