package com.example.demo.users.businesslayer.dtos;

import jakarta.validation.constraints.*;

public record CreateUserRequestDto(
        @Email(message = "Incorect email format")
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, message = "Message cannot be shorter than 8 symbols")
        @Size(max = 32, message = "Message cannot be longer than 32 symbols")
        String password,

        String username
) {}