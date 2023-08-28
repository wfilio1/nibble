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
                        "/api/recipes", "/api/recipes/*", "/api/ingredients", "/api/ingredients/*", "/api/measurements").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/api/pantry").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/currentUser").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.POST,
                        "/api/recipes").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.POST,
                        "/api/pantry").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET,
                        "/api/pantry/*").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,
                        "/api/recipes/*").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,
                        "/api/pantry/delete/*").hasAnyAuthority("USER")
                .antMatchers("/**").denyAll()
                .and()
                // New...
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
