package io.chaerin.backend.dao.adapter;

import io.chaerin.backend.dao.RefreshTokenBlackListRepository;
import io.chaerin.backend.dao.RefreshTokenRepository;
import io.chaerin.backend.dao.TokenRepository;
import io.chaerin.backend.domain.Member;
import io.chaerin.backend.domain.RefreshToken;
import io.chaerin.backend.domain.RefreshTokenBlackList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    private final EntityManager entityManager;

    @Override
    public RefreshToken save(Member member, String token) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                    .member(member)
                    .refreshToken(token)
                    .build()
        );

    }

    @Override
    public RefreshTokenBlackList addBlackList(RefreshToken refreshToken) {
        return refreshTokenBlackListRepository.save(
                RefreshTokenBlackList.builder()
                        .refreshToken(refreshToken)
                        .build()
        );

    }

    @Override
    public Optional<RefreshToken> findValidRefToken(Long memberId) {

        // 블랙리스트에 rt가 등록되어 있지 않다면, rt 조회
        String jpql = "select rt from RefreshToken rt left join RefreshTokenBlackList rtb on rtb.refreshToken = rt where rt.member.id = :memberId and rtb.id is null";


        return entityManager.createQuery(jpql, RefreshToken.class)
                .setParameter("memberId", memberId)
                .getResultStream()
                .findFirst();
    }
}
