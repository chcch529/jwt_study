package io.chaerin.backend.app;

import io.chaerin.backend.config.JwtConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

        String token = Jwts.builder()
                // payload에 들어날 내용
                .subject("Goood moring")
                // key value로 되어있는 body
                .claim("spring", "easy")
                // 발급된 시간
                .issuedAt(new Date())
                // 만료 시간 (일단 60초)
                .expiration(new Date(new Date().getTime() + jwtConfiguration.getValidation().getExp()))
                // 내가 만든거애옴. 서명 (암호화된 키, 어떻게 암호화 할건지)
                .signWith(Keys.hmacShaKeyFor("A5A10BC17B4E653C78BDD55FFFD9DEE3BE5790CD6F3F64DC1E82E791F9821C17".getBytes())
                        , Jwts.SIG.HS256)
                .compact();

        return token;
    }


}
