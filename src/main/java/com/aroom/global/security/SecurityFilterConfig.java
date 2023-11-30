package com.aroom.global.security;

import com.aroom.global.security.formlogin.CustomFormLoginFilter;
import com.aroom.global.security.jwt.JwtAuthenticationFilter;
import java.util.Arrays;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityFilterConfig {

	private final CustomFormLoginFilter customFormLoginFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityFilterConfig(
		CustomFormLoginFilter customFormLoginFilter,
		JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.customFormLoginFilter = customFormLoginFilter;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	SecurityFilterChain http(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.addExposedHeader("X-AUTH-TOKEN");
				return configuration;
            }))
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionConfig ->
				sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(request ->
			request
				.requestMatchers("/v1/reservations/**").authenticated()
				.requestMatchers("/v1/carts/**").authenticated()
				.anyRequest().permitAll()
		);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(customFormLoginFilter, JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web ->
			web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.requestMatchers("/favicon.ico", "/resources/**", "/error");
	}
}
