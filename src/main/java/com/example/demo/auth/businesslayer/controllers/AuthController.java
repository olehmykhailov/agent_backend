package com.example.demo.auth.businesslayer.controllers;


import com.example.demo.auth.businesslayer.dtos.SignInRequestDto;
import com.example.demo.auth.businesslayer.dtos.SignInResponseDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshRequestDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshResponseDto;
import com.example.demo.users.businesslayer.dtos.CreateUserRequestDto;
import com.example.demo.users.businesslayer.dtos.CreateUserResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.businesslayer.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto requestDto) {

        return authService.signIn(requestDto);
    }

    @PostMapping("/sign-up")
    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto requestDto) {
        return authService.signUp(requestDto);
    }

    @PostMapping("/refresh-token")
    public TokenRefreshResponseDto refreshToken(@RequestBody TokenRefreshRequestDto requestDto) {
        System.out.println(requestDto.userId());
        return authService.refresh(requestDto);
    }
}
