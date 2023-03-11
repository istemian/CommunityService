package com.lth.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI CommunityOpenAPI() {
    Info info = new Info().version("0.0.1").title("CommunityService").description("이태훈 개인 프로젝트");
    return new OpenAPI().info(info);
  }
}
