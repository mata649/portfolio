package com.mata649.portfolio.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {
    private final Environment environment;
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/v3/api-docs/**",
    };

    public HttpSecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequest -> {


                    httpRequest.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    // Skills
                    httpRequest.requestMatchers(HttpMethod.GET, "/skills").permitAll();
                    httpRequest.requestMatchers(HttpMethod.GET, "/skills/*").permitAll();

                    for (String profile : environment.getActiveProfiles()) {
                        if (profile.equals("dev")) {
                            httpRequest.requestMatchers(HttpMethod.GET, SWAGGER_WHITELIST).permitAll();
                        }
                    }
                    httpRequest.anyRequest().authenticated();
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}