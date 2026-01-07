package com.example.demo.users.businesslayer.dtos;


import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateUserResponseDto(
    @NotBlank
    UUID id,

    @NotBlank
    String email,

    String username
    ) {}

