package io.chaerin.backend.app;

import io.chaerin.backend.config.JwtConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfiguration jwtConfiguration;

    // application.yml에서 가져욤
    // 스프링이 가져와서 넣어주는 거라, 스프링 추가해줘야 가넝
//    @Value("${custom.jwt.validation.exp}")
//    private Long exp;
    public String issue() {

        return Jwts.builder()
                // payload에 들어날 내용
                .subject("Goood moring")
                // key value로 되어있는 body
                .claim("spring", "easy")
                // 발급된 시간
                .issuedAt(new Date())
                // 만료 시간 (일단 60초)
                .expiration(new Date(new Date().getTime() + jwtConfiguration.getValidation().getExp()))
                // 내가 만든거애옴. 서명 (암호화된 키, 어떻게 암호화 할건지)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }


    // jwt 토큰 만들 때만 쓸거니까 private여도 상관 ㄴㄴ
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecrets().getAppKey().getBytes());
    }


}
