package com.uber.clone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI uberCloneOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Uber Clone API")
                .description("REST API for Uber Clone Backend Application")
                .version("1.0.0")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@uberclone.com")));
    }
}