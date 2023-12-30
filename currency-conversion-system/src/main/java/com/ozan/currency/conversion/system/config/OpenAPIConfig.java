package com.ozan.currency.conversion.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

	@Value("${openapi.url}")
	private String url;

	@Bean
	OpenAPI myOpenAPI() {
		Server server = new Server();
		server.setUrl(url);
		server.setDescription("POC Server URL");

		Contact contact = new Contact();
		contact.setEmail("ozgurozbil@gmail.com");
		contact.setName("Özgür Özbil");

		Info info = new Info().title("Ozan Conversion Management System API").version("1.0.0").contact(contact);

		return new OpenAPI().info(info).addServersItem(server);
	}
}