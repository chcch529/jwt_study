package io.chaerin.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(httpB -> httpB.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .formLogin(form -> form.disable())
                // 세션을 어케 다룰지 지정
                .sessionManagement(session -> session.sessionCreationPolicy(
                        // 우리 세션 정책은 stateless야~ 더 이상 기억 안할거야~~
                        SessionCreationPolicy.STATELESS
                ))
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/user/**")
                                    .hasAnyAuthority("ADMIN","MEMBER")
                                .requestMatchers("/admin/**")
                                    .hasAnyAuthority("ADMIN")
                                .anyRequest()
                                    .authenticated()
                )
                .build();
    }

}
