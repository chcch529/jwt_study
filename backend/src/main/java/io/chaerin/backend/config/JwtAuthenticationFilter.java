package io.chaerin.backend.config;

import io.chaerin.backend.app.JwtTokenProvider;
import io.chaerin.backend.app.MemberService;
import io.chaerin.backend.domain.Member;
import io.chaerin.backend.dto.MemberDetails;
import io.chaerin.backend.dto.TokenBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    OncePerRequestFilter: 요청이 들어올 때 딱 한번만 동작

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validate(token)) {
            TokenBody tokenBody = jwtTokenProvider.parseJwt(token);

            MemberDetails memberDetails = memberService.getMemberDetailsById(tokenBody.getMemberId());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberDetails, token, memberDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 이거 해야 다음 필터로 넘어감~! 필수필수
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 요청을 받아서 header가 있따면 Bearer 빼고 토큰만
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
