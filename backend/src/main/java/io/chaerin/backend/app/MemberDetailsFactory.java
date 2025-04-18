package io.chaerin.backend.app;

import io.chaerin.backend.dto.MemberDetails;
import io.chaerin.backend.exceptions.UnavailableProviderException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class MemberDetailsFactory {

    public static MemberDetails memberDetails(String provider, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        switch (provider.toUpperCase()) {
            case "GOOGLE" -> {
                return MemberDetails.builder()
                        .name(attributes.get("name").toString())
                        .email(attributes.get("email").toString())
                        .attributes(attributes)
                        .build();
            }

            case "NAVER" -> {
                Map<String, Object> properties = (Map<String, Object>) attributes.get("response");

                return MemberDetails.builder()
                        .name(properties.get("name").toString())
                        .email(properties.get("id").toString() + "@naver.com")
                        .attributes(attributes)
                        .build();

            }

            case "KAKAO" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("properties");

                return MemberDetails.builder()
                        .name(properties.get("nickname"))
                        .email(attributes.get("id").toString() + "@kakao.com")
                        .attributes(attributes)
                        .build();

            }

            default -> throw new UnavailableProviderException("지원하지 않는 제공자: "+ provider);
        }
    }

}
