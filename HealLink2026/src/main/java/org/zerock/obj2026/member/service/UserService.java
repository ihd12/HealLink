package org.zerock.obj2026.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.domain.UserRole;
import org.zerock.obj2026.member.dto.UserDTO;
import org.zerock.obj2026.member.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserDTO dto){
        // 저장할 계정 데이터 설정
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail()) // email 설정
                // 비밀번호를 BCrypt방식으로 암호화 하여 설정
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(UserRole.PATIENT)
                .tel(dto.getTel())
                .build();
        // DB에 계정 저장 후 id값을 반환
        return userRepository.save(user).getUserId();
    }


}
