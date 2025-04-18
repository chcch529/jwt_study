package io.chaerin.backend.app;

import io.chaerin.backend.dto.Role;
import io.chaerin.backend.dto.TokenBody;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.grammars.ordering.OrderingLexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JwtTokenProviderTests {

    @Autowired
    JwtTokenProvider provider;

    @Test
    @DisplayName("jwt 발급 테스트")
    void jwt_issue_test() throws Exception {

        Long targetId = 1L;
        Role targetRole = Role.MEMBER;

        String token = provider.issueAccessToken(targetId, targetRole);
        log.info("token = {}", token);

    }


    @Test
    @DisplayName("토큰 유혀성 검사 테스트")
    void token_validate_test() throws Exception {
        String targetToken1 = "eyJhbGciJIINifJ9.eyJzdWIiOxwi9sZSI61FTUUiIslhdCTc0ND0NjNiiZwIjoNzQ0OQ2Ok2fQ.YUBvLn-sLe8tLCrFZJlGPO7S9YM_PRXQ";

        String targetToken2 = "gooodmoring";

        boolean validate1 = provider.validate(targetToken1);
        boolean validate2 = provider.validate(targetToken2);

        log.info("validate1 = {}", validate1);
        log.info("validate1 = {}", validate2);
    }

    @Test
    @DisplayName("jwt parse")
    void parse_jwt_test() throws Exception {

        // given
        Long targetId = 100L;
        Role targetRole = Role.MEMBER;

        String targetToken = provider.issueAccessToken(targetId, targetRole);

        boolean result = provider.validate(targetToken);
        assertThat(result).isTrue();

        // when
        TokenBody tokenBody = provider.parseJwt(targetToken);

        // then
        assertThat(tokenBody.getMemberId()).isEqualTo(targetId);
        assertThat(tokenBody.getRole()).isEqualTo(targetRole);
    }
}