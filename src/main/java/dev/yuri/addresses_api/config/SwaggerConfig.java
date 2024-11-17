package dev.yuri.addresses_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gerenciamento de Endereços",
        version = "v1.0",
        description =
            "API destinada ao gerenciamento de entidades relacionadas a endereços, " +
            "como estados (UF), municípios, bairros e pessoas.",
        contact = @Contact(name = "Yuri Italo", email = "yuri.italo94@gmail.com"),
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local")
        }
)
public class SwaggerConfig {

}