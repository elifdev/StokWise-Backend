package com.tobeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(authorize -> authorize
//				.requestMatchers("**").permitAll()
//				.requestMatchers("/api/v1/login","/api/v1/user/signup").permitAll()
				.anyRequest().permitAll() //.authenticated()
				)
		     .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// @formatter:on

		return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
