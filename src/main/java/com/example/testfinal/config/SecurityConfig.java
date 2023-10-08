package com.example.testfinal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/person").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/person/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/upload").hasAnyRole("ADMIN", "IMPORTER")
                .requestMatchers(HttpMethod.POST, "/person/employee/{id}/job").hasAnyRole("ADMIN", "EMPLOYEE")
                .and()
                .httpBasic().and()
                .csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .and()
                .withUser("employee")
                .password(passwordEncoder.encode("employee"))
                .roles("EMPLOYEE")
                .and()
                .withUser("importer")
                .password(passwordEncoder.encode("importer"))
                .roles("IMPORTER");
    }

}
