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
    Info info = new Info().version("0.0.1").title("CommunityService").description("이태훈 개인 프로젝트");
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
