package vincent.project_interior.global.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileStorageService implements FileStorageService {

  private final Path uploadDir;

  public LocalFileStorageService(@Value("${app.upload.dir}") String uploadDir) {
    this.uploadDir = Path.of(uploadDir);
  }

  @Override
  public String store(MultipartFile file) {
    try {
      Files.createDirectories(uploadDir);
      String extension = extractExtension(file.getOriginalFilename());
      String storedName = UUID.randomUUID() + extension;
      file.transferTo(uploadDir.resolve(storedName));
      return storedName;
    } catch (IOException e) {
      throw new UncheckedIOException("파일 저장에 실패했습니다.", e);
    }
  }

  @Override
  public byte[] load(String storedPath) {
    try {
      return Files.readAllBytes(uploadDir.resolve(storedPath));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 읽기에 실패했습니다.", e);
    }
  }

  private String extractExtension(String originalFilename) {
    if (originalFilename == null) {
      return "";
    }
    int dotIndex = originalFilename.lastIndexOf('.');
    return dotIndex >= 0 ? originalFilename.substring(dotIndex) : "";
  }
}
