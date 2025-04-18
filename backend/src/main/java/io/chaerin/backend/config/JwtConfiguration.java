package io.chaerin.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtConfiguration {

    // [2. 생성된 validation을 여기에 넣음]
    private final Validation validation;
    private final Secrets secrets;

    // [1. 먼저 validation이 생성됨]
    @Getter
    @RequiredArgsConstructor
    public static class Validation {
        private final Long access;
        private final Long refresh;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Secrets {
        private final String originKey;
        private final String appKey;
    }
}
