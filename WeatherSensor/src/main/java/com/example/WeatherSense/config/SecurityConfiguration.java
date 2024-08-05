package com.example.WeatherSense.config;

import com.example.WeatherSense.security.AccessDeniedHandlerImpl;
import com.example.WeatherSense.security.AuthProviderImpl;
import com.example.WeatherSense.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JWTFilter jwtFilter;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    public SecurityConfiguration(JWTFilter jwtFilter, AccessDeniedHandlerImpl accessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/admin/allSensors").hasRole("ADMIN")
                        .requestMatchers("/admin/getSensorById/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/getPersonById/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/delSensorById/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/delPersonById/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/appointAdminById/{id}").hasRole("ADMIN")
                        .requestMatchers("/auth/login","/auth/registration" ,"/error").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm-> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(PersonDetailsService personDetailsService, PasswordEncoder passwordEncoder) {
        return new AuthProviderImpl(personDetailsService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
