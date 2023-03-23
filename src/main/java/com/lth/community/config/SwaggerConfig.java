package com.lth.community.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI CommunityOpenAPI() {
    Info info = new Info().version("0.0.1").title("CommunityService").description("""
            이태훈 개인 프로젝트로 AWS를 이용해 배포한 swagger 주소입니다.
            AWS RDS를 이용하여 DB관리 및 EC2를 이용하여 배포, Spring Security를 이용한 AccessToken 구현,
            비밀번호 암호화 및 권한 설정, Scheduler를 이용해 일정시간마다 method 실행,
            비밀번호 찾기 시 메일로 임시비밀번호 보내기 등 기본적인 CRUD 및 다양한 기능 구현했습니다.
            로그인 시 발급받은 accessToken을 우측 상단의 Authorize 버튼을 눌러 Value 값에 입력하여 로그인이 가능합니다
            Github 주소는 https://github.com/kleenex4770/CommunityService 입니다.""");
    final String securitySchemeName = "bearerAuth";
    OpenAPI openAPI = new OpenAPI().info(info);
    openAPI.addSecurityItem(new SecurityRequirement()
                    .addList(securitySchemeName))
            .components(new Components()
                    .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")));
    return openAPI;
  }
}
