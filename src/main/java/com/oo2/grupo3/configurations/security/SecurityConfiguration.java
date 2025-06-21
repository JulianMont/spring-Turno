package com.oo2.grupo3.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


import com.oo2.grupo3.services.implementations.UserServiceImp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
            	// Recursos públicos
            	auth.requestMatchers("/css/**", "/js/**", "/imgs/**", "/vendor/**").permitAll();

            	// Páginas públicas
            	auth.requestMatchers("/auth/login", "/auth/loginProcess", "/auth/register", "/auth/registerProcess", "/auth/logout", "/home/index").permitAll();

            	// Acceso común a ambos roles
            	auth.requestMatchers("/especialidades/list", "/servicios/list", "/turnos/**").hasAnyRole("USER", "ADMIN");

            	// Todo lo demás solo ADMIN
            	auth.anyRequest().hasRole("ADMIN");

            })
            .formLogin(login -> {
                login.loginPage("/auth/login");
                login.loginProcessingUrl("/auth/loginProcess");
                login.usernameParameter("username");
                login.passwordParameter("password");
                login.defaultSuccessUrl("/home/index", true);
                login.permitAll();
            })
            .logout(logout -> {
                logout.logoutUrl("/auth/logout");
                logout.logoutSuccessUrl("/auth/login");
                logout.permitAll();
            })
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


