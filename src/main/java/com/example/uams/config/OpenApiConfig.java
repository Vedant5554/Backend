package com.example.uams.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Swagger UI at http://localhost:8080/swagger-ui.html
 *
 * Adds a "Authorize" button to Swagger UI so you can paste your JWT
 * and test protected endpoints directly from the browser.
 *
 * Usage:
 *  1. POST /api/auth/login  → copy the token from the response
 *  2. Click "Authorize" in Swagger UI
 *  3. Enter:  Bearer <your-token>
 *  4. All subsequent requests will include the Authorization header
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Paste your JWT token here. " +
                                                "Get it from POST /api/auth/login")));
    }

    private Info apiInfo() {
        return new Info()
                .title("UAMS — University Accommodation Management System")
                .version("1.0.0")
                .description("REST API for managing student accommodation, " +
                        "leases, invoices, halls, apartments and reports.")
                .contact(new Contact()
                        .name("UAMS Dev Team")
                        .email("dev@uams.com"));
    }
}