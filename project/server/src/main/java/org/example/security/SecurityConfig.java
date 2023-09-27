package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {

        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers("/authenticate").permitAll() // authenticates user needed to provide JWT
                .antMatchers("/create_account").permitAll() // added for account creation
                .antMatchers(HttpMethod.GET,
                        "/api/recipes", "/api/recipes/*", "/api/pantry", "/api/ingredients", "/api/ingredients/*", "/api/measurements", "/api/comments/*").permitAll()
                .antMatchers(HttpMethod.POST,
                        "/api/recipes", "/api/pantry", "/api/comments", "/api/liked").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET,
                        "/api/pantry/*", "/api/liked/personal").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/api/recipes/*", "/api/pantry/delete/*", "/api/comments/*", "/api/liked/*", "ADMIN").hasAnyAuthority("USER")
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfig), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
