package com.sadatscode.securityservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info=@Info(
                contact = @Contact(
                        name = "Sedat",
                        email = "memmedlisedat033@gmail.com",
                        url = "https://github.com/SedatMammadly"
                ),
                description = "Security service for ecommerce-backend project",
                title = "Open api for Sedat",
                license = @License(
                        url = "https://github.com/SedatMammadly"
                )
        ),
        servers = {
                @Server(
                        description = "security-service",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {

}
