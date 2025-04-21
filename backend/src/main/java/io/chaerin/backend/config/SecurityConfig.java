package io.chaerin.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth -> {
                    // 인증 후 다 성공했을 때 어떤 작업을 수행할 지 정의
                    oauth.successHandler(oAuth2SuccessHandler);
                })
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/user/**")
                                    .hasAnyAuthority("ADMIN","MEMBER")
                                .requestMatchers("/admin/**")
                                    .hasAnyAuthority("ADMIN")
                                .anyRequest()
                                    .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
