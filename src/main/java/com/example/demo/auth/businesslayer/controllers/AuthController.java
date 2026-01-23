package com.example.demo.auth.businesslayer.controllers;


import com.example.demo.auth.businesslayer.dtos.SignInRequestDto;
import com.example.demo.auth.businesslayer.dtos.SignInResponseDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshRequestDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshResponseDto;
import com.example.demo.users.businesslayer.dtos.CreateUserRequestDto;
import com.example.demo.users.businesslayer.dtos.CreateUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auth.businesslayer.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public SignInResponseDto signIn(@RequestBody SignInRequestDto requestDto) {
        return authService.signIn(requestDto);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto requestDto) {
        return authService.signUp(requestDto);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenRefreshResponseDto refreshToken(@RequestBody TokenRefreshRequestDto requestDto) {
        return authService.refresh(requestDto);
    }
}
