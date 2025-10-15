package ru.dip4rip.musicservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.dip4rip.musicservice.service.CustomUserDetailsService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                // Разрешаем доступ к Swagger UI и API документации
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Разрешаем только POST запросы к /api/user (регистрация) без авторизации
                .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                // GET запросы к /api/user требуют авторизации
                .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole("USER", "ADMIN")
                // Админские эндпоинты для управления пользователями
                .requestMatchers("/api/admin/users/**").hasRole("ADMIN")
                // Админские эндпоинты для управления музыкой
                .requestMatchers("/api/admin/music/**").hasRole("ADMIN")
                // Обычные пользователи могут просматривать музыку
                .requestMatchers("/api/music/**").hasAnyRole("USER", "ADMIN")
                // Пользователи и администраторы могут управлять плейлистами
                .requestMatchers("/api/playlist/**").hasAnyRole("USER", "ADMIN")
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic
                .realmName("Music Service")
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
