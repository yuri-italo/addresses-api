package dev.yuri.addresses_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "API de Gerenciamento de Endereços",
		version = "v1.0",
		description = "API destinada ao gerenciamento de entidades relacionadas a endereços, como estados (UF), municípios, bairros e pessoas.",
		contact = @Contact(name = "Yuri Italo", email = "yuri.italo94@gmail.com")
	)
)

public class AddressesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressesApiApplication.class, args);
	}

}
