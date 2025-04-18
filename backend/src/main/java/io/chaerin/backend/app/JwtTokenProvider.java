package io.chaerin.backend.app;

import io.chaerin.backend.config.JwtConfiguration;
import io.chaerin.backend.dto.Role;
import io.chaerin.backend.dto.TokenBody;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration jwtConfiguration;

    // application.yml에서 가져욤
    // 스프링이 가져와서 넣어주는 거라, 스프링 추가해줘야 가넝
//    @Value("${custom.jwt.validation.exp}")
//    private Long exp;

    public String issueAccessToken(Long id, Role role) {
        return issue(id, role, jwtConfiguration.getValidation().getAccess());
    }
    public String issueRefreshToken(Long id, Role role) {
        return issue(id, role, jwtConfiguration.getValidation().getRefresh());
    }


    private String issue(Long id, Role role, Long expTime) {

        return Jwts.builder()
                // payload에 들어날 내용
                .subject(id.toString())
                // key value로 되어있는 body
                .claim("role", role)
                // 발급된 시간
                .issuedAt(new Date())
                // 만료 시간
                .expiration(new Date(new Date().getTime() + expTime))
                // 내가 만든거애옴. 서명 (암호화된 키, 어떻게 암호화 할건지)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    // 유효 토큰인지 확인
    public boolean validate(String token) {

        try {

            // jwt 분석
            Jwts.parser()
                    // 어떤걸로 서명했는지 넣엉줌
                    .verifyWith(getSecretKey())
                    .build()
                    // 내가 토큰에 넣었던 claim을 반환해줌(Jws<Claims>) -> 유효성 검사.. 이상하면 예외 던짐
                    .parseSignedClaims(token);


            return true;

        } catch (JwtException e) { // jwt 자체가 이상.
            log.error("token = {}", token);
            log.error("잘못된 토큰이 입력됨");

        } catch (IllegalStateException e) { // 그 외 토큰 err
            log.error("token = {}", token);
            log.error("이상한 토큰이 입력됨");

        } catch (Exception e) {
            log.error("token = {}", token);
            log.info(";;");
        }

        return false;
    }

    public TokenBody parseJwt(String token) {

        // validate가 선행된 token을 사용할 거니까
        // 여기서까지 예외 터지면 터트려야함.
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        String subject = parsed.getPayload().getSubject();
        String role = parsed.getPayload().get("role").toString();

        return new TokenBody(
                Long.parseLong(subject), Role.valueOf(role)
        );
    }


    // jwt 만들 때만 쓸거니까 private여도 상관 ㄴㄴ
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecrets().getAppKey().getBytes());
    }


}
