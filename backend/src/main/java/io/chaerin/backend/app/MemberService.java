package io.chaerin.backend.app;

import io.chaerin.backend.domain.Member;
import io.chaerin.backend.dao.MemberRepository;
import io.chaerin.backend.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        MemberDetails memberDetails = MemberDetailsFactory.memberDetails(provider, oAuth2User);

        Optional<Member> memberOptional = memberRepository.findByEmail(memberDetails.getEmail());

        Member findMember = memberOptional.orElseGet(() -> {
            Member member = Member.builder()
                    .email(memberDetails.getEmail())
                    .provider(provider)
                    .username(memberDetails.getName())
                    .build();

            return memberRepository.save(member);
        });

        if (findMember.getProvider().equals(provider)) {
            return memberDetails.setId(findMember.getId()).setRole(findMember.getRole());
        } else {
            throw new IllegalStateException("다른 이메일로 가입되어 있습니다.");
        }

    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member getById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public MemberDetails getMemberDetailsById(Long id) {

        MemberDetails memberDetails = MemberDetails.from(getById(id));

        return memberDetails;

    }
}
