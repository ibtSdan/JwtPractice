package com.example.JwtPractice.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeResponse {
    private String name;

    private String status;

    private LocalDateTime registeredAt;
}
