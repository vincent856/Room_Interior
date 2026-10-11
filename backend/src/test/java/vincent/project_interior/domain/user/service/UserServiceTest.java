package vincent.project_interior.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import vincent.project_interior.domain.user.dto.request.SignUpRequest;
import vincent.project_interior.domain.user.dto.response.UserResponse;
import vincent.project_interior.domain.user.exception.UserErrorCode;
import vincent.project_interior.domain.user.exception.UserException;

@SpringBootTest
@Transactional
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void 회원가입하면_이메일이_소문자로_정규화되어_저장된다() {
    UserResponse response = userService.signUp(
        new SignUpRequest("Test@Example.com", "password1", "닉네임"));

    assertThat(response.email()).isEqualTo("test@example.com");
    assertThat(response.nickname()).isEqualTo("닉네임");
  }

  @Test
  void 이미_가입된_이메일로_가입하면_DUPLICATE_EMAIL_예외가_발생한다() {
    userService.signUp(new SignUpRequest("dup@example.com", "password1", "유저1"));

    assertThatThrownBy(() -> userService.signUp(
        new SignUpRequest("DUP@example.com", "password2", "유저2")))
        .isInstanceOf(UserException.class)
        .extracting(e -> ((UserException) e).getErrorCode())
        .isEqualTo(UserErrorCode.DUPLICATE_EMAIL);
  }
}
