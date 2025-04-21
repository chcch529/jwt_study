package io.chaerin.backend.dto;

import io.chaerin.backend.domain.Member;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetails implements OAuth2User {

    @Setter
    private Long id;

    private String name;
    private String email;

    @Setter
    private Role role;

    @Setter
    private Map<String, Object> attributes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public static MemberDetails from(Member member) {
        MemberDetails memberDetails = new MemberDetails();

        memberDetails.name = member.getUsername();
        memberDetails.email = member.getEmail();
        memberDetails.role = member.getRole();

        return memberDetails;
    }

    // member 엔티티로부터 만들어지는 dto 객체......

    @Builder
    public MemberDetails(String name, String email, Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }
}
