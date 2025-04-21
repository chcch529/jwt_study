package io.chaerin.backend.config;

import io.chaerin.backend.app.JwtTokenProvider;
import io.chaerin.backend.app.MemberService;
import io.chaerin.backend.domain.Member;
import io.chaerin.backend.domain.RefreshToken;
import io.chaerin.backend.dto.MemberDetails;
import io.chaerin.backend.dto.TokenPair;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${custom.jwt.redirection.base}")
    private String baseUrl;


    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // memberDetails 인스턴스가 저장됨
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole());

        // 최초로그인: accessToken, refreshToken 같이 생성
        // - 근데? pc로 최초 로그인 후 mobile로 최초 로그인이면, 중복 생성될 수 있음 ㄷㄷ
        // - 그러면, 두번째 최초 로그인 시에는 이메일로 rt을 db에서 찾아오고, at만 새로 생성해서 주면 됨.
        // 최초 로그임 == 살아있는 rt가 없는 상태

        Member findMember = memberService.getById(principal.getId());

        HashMap<String, String> params = new HashMap<>();

        Optional<RefreshToken> refreshTokenOptional = jwtTokenProvider.findRefreshToken(principal.getId());

        if (refreshTokenOptional.isEmpty()) {
            TokenPair tokenPair = jwtTokenProvider.generateTokenPair(findMember);
            params.put("access", tokenPair.getAccessToken());
            params.put("refresh", tokenPair.getRefreshToken());

        } else {
            String accessToken = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole());
            params.put("access", accessToken);
            params.put("refresh", refreshTokenOptional.get().getRefreshToken());
        }


        // url 포맷 맞춰서 자동 인코딩(?) 고런 느낌
        String urlStr = genUrlStr(params);

        getRedirectStrategy().sendRedirect(request, response, urlStr);


    }

    private String genUrlStr(HashMap<String, String> params ) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("access", params.get("access"))
                .queryParam("refresh", params.get("refresh"))
                .build()
                .toUri()
                .toString();
    }
}
