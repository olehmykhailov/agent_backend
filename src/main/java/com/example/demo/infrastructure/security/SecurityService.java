package com.example.demo.infrastructure.security;

import com.example.demo.infrastructure.errors.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Service
public class SecurityService {

    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth instanceof AnonymousAuthenticationToken ||
                auth.getName() == null
        ) {
            throw new UnauthorizedException("User not authenticated");
        }

        return UUID.fromString(auth.getName());
    }
}



