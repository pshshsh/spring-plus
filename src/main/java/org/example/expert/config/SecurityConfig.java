package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 활성화
@EnableMethodSecurity(securedEnabled = true) // 어노테이션 기반 권한 체크 기능 활성화
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  // Spring Security에서 사용자 인증시 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        // jwtAuthenticationFilter 추가
        .addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .rememberMe(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(request -> request.getRequestURI().startsWith("/auth")).permitAll()
            .requestMatchers("/test").hasAuthority(UserRole.Authority.ADMIN)
            .requestMatchers("/open").permitAll()
            .requestMatchers("/health").permitAll()
            .anyRequest().authenticated()
        )
        .build();
  }
}