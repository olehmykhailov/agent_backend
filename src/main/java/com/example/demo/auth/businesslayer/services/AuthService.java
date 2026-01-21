package com.example.demo.auth.businesslayer.services;

import com.example.demo.auth.businesslayer.dtos.SignInRequestDto;
import com.example.demo.auth.businesslayer.dtos.SignInResponseDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshRequestDto;
import com.example.demo.auth.businesslayer.dtos.TokenRefreshResponseDto;
import com.example.demo.auth.datalayer.entities.RefreshTokenEntity;
import com.example.demo.infrastructure.errors.*;
import com.example.demo.users.businesslayer.services.UsersService;
import com.example.demo.users.businesslayer.dtos.CreateUserResponseDto;
import com.example.demo.users.businesslayer.dtos.CreateUserRequestDto;
import com.example.demo.users.datalayer.entities.UserEntity;
import com.example.demo.auth.businesslayer.jwt.JwtTokenProvider;
import com.example.demo.auth.datalayer.repositories.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
public class AuthService {
    private final long refreshTokenValidityMs;

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            @Value("${jwt.refresh.expiration}") long refreshTokenValidityMs,
            UsersService usersService,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenValidityMs = refreshTokenValidityMs;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(UserEntity user,
                                     long validitySeconds) {


        String rawToken = UUID.randomUUID().toString();


        String tokenHash = passwordEncoder.encode(rawToken);


        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setIssuedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(validitySeconds));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);


        return rawToken;
    }

    @Transactional
    public TokenRefreshResponseDto refresh(
            TokenRefreshRequestDto request
    ) {

        List<RefreshTokenEntity> tokens =
                refreshTokenRepository.findAllByUserIdAndRevokedFalse(
                        UUID.fromString(request.userId())
                );

        if (tokens.isEmpty()) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        RefreshTokenEntity token = tokens.stream()
                .filter(t -> passwordEncoder.matches(
                        request.refreshToken(),
                        t.getTokenHash()
                ))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));


        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            token.setRevoked(true);
            throw new UnauthorizedException("Refresh token expired");
        }




        token.setRevoked(true);

        String newRefresh = createRefreshToken(
                token.getUser(),
                refreshTokenValidityMs / 1000
        );

        String newAccess =
                jwtTokenProvider.generateAccessToken(token.getUser().getId());

        return new TokenRefreshResponseDto(newAccess, newRefresh);
    }


    public CreateUserResponseDto signUp(CreateUserRequestDto createUserRequestDto) {
        if (usersService.existsByEmail(createUserRequestDto.email())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(createUserRequestDto.password());

        UserEntity userEntity = usersService.createUser(createUserRequestDto, encodedPassword);

        return new CreateUserResponseDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername()
        );
    }

    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        UserEntity userEntity = usersService.getUserByEmail(signInRequestDto.email());

        if (!passwordEncoder.matches(signInRequestDto.password(), userEntity.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }


        String accessToken = jwtTokenProvider.generateAccessToken(userEntity.getId());
        String refreshToken = createRefreshToken(
                userEntity,
                refreshTokenValidityMs / 1000
        );

        return new SignInResponseDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                accessToken,
                refreshToken
        );
    }

}
