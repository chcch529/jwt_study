package io.chaerin.backend.dao;

import io.chaerin.backend.domain.Member;
import io.chaerin.backend.domain.RefreshToken;
import io.chaerin.backend.domain.RefreshTokenBlackList;

import java.util.Optional;

public interface TokenRepository {
    // rt랑 b가 생명주기가 같으니까 레포지토리를 합쳐보곘다

    RefreshToken save(Member member, String token);
    RefreshTokenBlackList addBlackList(RefreshToken refreshToken);

    Optional<RefreshToken> findValidRefToken(Long memberId);

}
