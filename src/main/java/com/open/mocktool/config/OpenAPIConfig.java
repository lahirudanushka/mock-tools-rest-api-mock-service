package com.open.mocktool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@OpenAPIDefinition(
        info = @Info(title = "Mock Tools",
                version = "V1.0.0",
                description = "Accelerate your development with our free mock API service. Quickly create realistic REST APIs without coding. Perfect for frontend development, API testing, and documentation. Customize responses, simulate complex behaviors, and generate dynamic data effortlessly. Start prototyping and testing today!.\n\n" +
                        "Important!! : Authentication is required on this platform to manage mocks and users. To proceed, please navigate to the Authentication Controller for detailed instructions. Managing users requires an ADMIN role, while overseeing mocks requires a USER role. If you do not have an account to login, please contact me directly for assistance.",
                contact = @Contact(name = "Author/Admin - Lahiru Danushka", email = "hirud94@gmail.com")

        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {


}
