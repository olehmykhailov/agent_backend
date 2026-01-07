package com.example.demo.auth.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshResponseDto(
        @NotBlank
        String accessToken,

        @NotBlank
        String refreshToken

) {}
