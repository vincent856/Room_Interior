package vincent.project_interior.domain.user.service;

import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vincent.project_interior.domain.user.dto.request.SignUpRequest;
import vincent.project_interior.domain.user.dto.response.UserResponse;
import vincent.project_interior.domain.user.entity.User;
import vincent.project_interior.domain.user.exception.UserErrorCode;
import vincent.project_interior.domain.user.exception.UserException;
import vincent.project_interior.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponse signUp(SignUpRequest request) {
    String normalizedEmail = request.email().toLowerCase(Locale.ROOT);
    log.debug("회원가입 시도: email={}", normalizedEmail);
    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
    }
    String encodedPassword = passwordEncoder.encode(request.password());
    User user = User.createUser(normalizedEmail, encodedPassword, request.nickname());
    User saved = userRepository.save(user);
    log.info("회원가입 완료: userId={}", saved.getId());
    return UserResponse.from(saved);
  }

  public UserResponse getMe(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    return UserResponse.from(user);
  }
}
