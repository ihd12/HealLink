package org.zerock.obj2026.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.member.domain.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private UserRole role; // Using the enum
    private String tel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    }

