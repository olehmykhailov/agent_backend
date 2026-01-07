package com.example.demo.auth.businesslayer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SignInResponseDto(
        @NotBlank
        UUID id,

        @Email
        @NotBlank
        String email,

        String username,

        @NotBlank
        String accessToken,

        @NotBlank
        String refreshToken

) {
}
