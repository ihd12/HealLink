package org.zerock.obj2026.member.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.domain.UserRole;
import org.zerock.obj2026.member.dto.UserSecurityDTO;
import org.zerock.obj2026.member.repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        OAuth2User oAuth2user = super.loadUser(userRequest);
//      소셜 로그인에 사용한 회사 이름을 저장
        String company = userRequest.getClientRegistration().getRegistrationId();
        UserSecurityDTO userSecurityDTO = null;
        if (company.equals("kakao")) {
            Map<String, Object> props = oAuth2user.getAttributes();
            LinkedHashMap<String, Object> profile =
                    (LinkedHashMap<String, Object>) props.get("properties");
            String nickname = profile.get("nickname").toString();
            User user = userRepository.findByEmail(nickname).orElse(null);
            if (user == null) {
//             소셜 로그인 처음 사용자인 경우
                user = userRepository.save(User.builder()
                        .email(nickname)
                        .password(passwordEncoder.encode("1234"))
                        .role(UserRole.PATIENT) //user권한 고정
                        .tel("010-1234-5678")
                        .build());
            }
            userSecurityDTO = new UserSecurityDTO(user, props);
        } else if (company.equals("google")) {
//              구글 소셜 로그인 처리
        }
        return userSecurityDTO;
    }
}
