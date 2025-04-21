package io.chaerin.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenBlackList {

    @Id
    @Column(name = "refresh_black_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 토큰으로 블랙리스트를 알 필요는 없으니 단방향임. (블랙리스트에서 토큰은 알아야해)
    // manytoone -> rt가 db에서 지워지지 않을거임..
    // 한 명의 회원이 여러 rt를 갖고 있을 수도 있음...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;


    // 토근 죽은 날
    private LocalDateTime createdAt = LocalDateTime.now();


    @Builder
    public RefreshTokenBlackList(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
