package com.example.JwtPractice.db;

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
    private String name;
    private String password;
    private String email;

    private LocalDateTime registerAt;
}
