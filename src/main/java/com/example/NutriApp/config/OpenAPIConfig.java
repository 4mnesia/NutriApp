package com.example.NutriApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NutriApp API")
                        .version("1.0.0")
                        .description("API REST para gestión de nutrición y comidas. Backend completo con modelos de Usuario, Alimento, Comida y ComidaAlimento.")
                        .contact(new Contact()
                                .name("NutriApp Team")
                                .email("info@nutriapp.com")
                                .url("https://github.com/4mnesia/NutriApp"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local de Desarrollo"),
                        new Server()
                                .url("https://api.nutriapp.com")
                                .description("Servidor de Producción")
                ));
    }
}
