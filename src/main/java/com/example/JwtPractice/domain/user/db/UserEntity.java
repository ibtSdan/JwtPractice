package com.example.JwtPractice.domain.user.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    private Long id;
    private String name;
    private String password;
    private String email;

    private LocalDateTime registerAt;
}
