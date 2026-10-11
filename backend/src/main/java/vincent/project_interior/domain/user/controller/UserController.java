package vincent.project_interior.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vincent.project_interior.domain.user.dto.request.SignUpRequest;
import vincent.project_interior.domain.user.dto.response.UserResponse;
import vincent.project_interior.domain.user.service.UserService;
import vincent.project_interior.global.security.SecurityUtils;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMe() {
    return ResponseEntity.ok(userService.getMe(SecurityUtils.currentUserId()));
  }
}
