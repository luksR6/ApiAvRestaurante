package com.senac.ApiAvRestaurante.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                                .requestMatchers("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/usuarios/admin-normal").hasRole("ADMIN_GERAL")
                                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("ADMIN_GERAL")
                                .requestMatchers(HttpMethod.POST, "/restaurantes").hasRole("ADMIN_NORMAL")
                                .requestMatchers(HttpMethod.PUT, "/restaurantes/**").hasRole("ADMIN_NORMAL")
                                .requestMatchers(HttpMethod.DELETE, "/restaurantes/**").hasRole("ADMIN_NORMAL")
                                .requestMatchers("/avaliacao/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/restaurantes/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
