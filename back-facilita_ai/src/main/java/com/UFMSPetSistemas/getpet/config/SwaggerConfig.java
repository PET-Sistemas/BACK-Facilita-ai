package com.UFMSPetSistemas.getpet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Facilita AI API")
                        .version("1.0.0")
                        .description("Documentação da API do projeto Facilita AI"))
                .addSecurityItem(new SecurityRequirement().addList("FacilitaAiScheme"))
                .components(new Components().addSecuritySchemes("FacilitaAiScheme", new SecurityScheme()
                        .name("FacilitaAiScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}