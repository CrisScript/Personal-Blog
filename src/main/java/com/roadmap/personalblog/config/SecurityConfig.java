package com.roadmap.personalblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // público
                        .requestMatchers("/", "/index.html", "/article.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()

                        // PROTEGER admin en ambas rutas
                        .requestMatchers("/admin", "/admin.html").authenticated()

                        // PROTEGER escritura API
                        .requestMatchers(HttpMethod.POST, "/api/articles/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/articles/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/**").authenticated()

                        // cierra todo
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults()); // pide user/pass en el navegador
        return http.build();
    }

    @Bean
    UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build()
        );
    }
}

