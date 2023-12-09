package com.fastcampus.mini9.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${remote-server.front.url}")
	private String frontUrl;
	@Value("${remote-server.gateway.url}")
	private String gatewayUrl;
	private String frontUrlLocal = "http://localhost:3000";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			//            .allowedOrigins(frontUrl, gatewayUrl, frontUrlLocal)
			.allowedHeaders("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowCredentials(true)
			.maxAge(6000);
	}
}
