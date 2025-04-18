package io.chaerin.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JwtConfigurationTests {

    @Autowired
    JwtConfiguration jwtConfiguration;

    @Test
    @DisplayName("jwt configuration key 값 확인")
    void jwt_config_test() throws Exception {
        String appKey = jwtConfiguration.getSecrets().getAppKey();
        log.info("appKey = {}", appKey);
    }
}