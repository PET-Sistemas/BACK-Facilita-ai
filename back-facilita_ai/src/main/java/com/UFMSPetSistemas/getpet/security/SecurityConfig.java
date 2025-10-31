package com.UFMSPetSistemas.getpet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        // endpoints mapeados por permissão
                        .requestMatchers(HttpMethod.DELETE, "/categoria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/categoria", "/categoria/id").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/categoria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categoria").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/usuario").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuario/todos", "/usuario/nome", "/usuario/endereco")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuario/id").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/usuario").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuario").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/prestacoes-servico").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/prestacoes-servico").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/prestacoes-servico/todos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/prestacoes-servico/usuario/id").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/prestacoes-servico/avaliacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/prestacoes-servico/id").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/prestacoes-servico").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/servico").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/servico").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servico").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/servico/valor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/servico/usuario-endereco").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/servico/todos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servico/id").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/servico/categoria/id").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // retorna a senha criptografada
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
