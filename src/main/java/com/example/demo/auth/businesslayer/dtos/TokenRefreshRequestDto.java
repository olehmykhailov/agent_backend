package com.example.demo.auth.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequestDto(
        @NotBlank
        String refreshToken,

        @NotBlank
        String userId
) {}
