package com.example.demo.auth.businesslayer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequestDto(
   @Email
   @NotBlank
   String email,

   @NotBlank
   String password
) {}
