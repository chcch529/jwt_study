package io.chaerin.backend.config;

import io.chaerin.backend.app.JwtTokenProvider;
import io.chaerin.backend.app.MemberService;
import io.chaerin.backend.dto.MemberDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService service;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // memberDetails 인스턴스가 저장됨
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole());

    }
}
