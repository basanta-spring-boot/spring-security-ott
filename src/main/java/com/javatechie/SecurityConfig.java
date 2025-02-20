package com.javatechie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/ott/sent").permitAll()
                                .requestMatchers("/login/ott").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .oneTimeTokenLogin(Customizer.withDefaults());
        return http.build();
    }

    /*
        In this code, the string {noop}password indicates that the password
        is stored as plain text, without any encoding.
        The {noop} prefix tells Spring Security that no password encoder should be applied,
        meaning that the password is not hashed or encrypted.
        This is typically used for testing or development purposes only.
    */
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.withUsername("javatechie")
                .password("{noop}password")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
