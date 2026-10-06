package vincent.project_interior.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 웹 브라우저에서 /uploads/**로 시작하는 요청이 들어오면
    registry.addResourceHandler("/uploads/**")
        // 실제 프로젝트 폴더 최상단의 uploads/ 폴더에서 파일 찾기
        .addResourceLocations("file:uploads/");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 인증 도입 전 로컬 개발 단계 한정 permissive 설정 - Security 도입 시 재검토 필요
    registry.addMapping("/api/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
  }
}
