package com.fastcampus.mini9.config.swagger;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Profile("!local")
@OpenAPIDefinition(
	 servers = {
	 @Server(url = "https://anti-bias.kr/api", description = "Server")
	 },
	info = @Info(
		title = "Accommodation Reservation Platform API",
		description = "Accommodation Reservation Platform를 위한 API",
		version = "v1",
		contact = @Contact(name = "고동훤", email = "donghar@naver.com", url = "https://github.com/Dr-KoKo")
	)
)
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		String authorizationHeader = "Authorization";
		String refreshSchemeName = "refresh-token";

		SecurityScheme jwtScheme = new SecurityScheme()
			.name(authorizationHeader)
			.type(Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT");
		SecurityScheme refreshScheme = new SecurityScheme()
			.name(refreshSchemeName)
			.type(Type.APIKEY)
			.in(In.COOKIE);
		Components components = new Components()
			.addSecuritySchemes(authorizationHeader, jwtScheme)
			.addSecuritySchemes(refreshSchemeName, refreshScheme);

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList(authorizationHeader)
			.addList(refreshSchemeName);

		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(components);
	}
}
