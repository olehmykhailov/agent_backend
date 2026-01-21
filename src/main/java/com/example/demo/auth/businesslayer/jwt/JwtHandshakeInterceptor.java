package com.example.demo.auth.businesslayer.jwt;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String token = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("token");
            System.out.println(request.getURI());
            System.out.println("Token received: " + (token != null));

            if (token == null) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return false;
            }

            UUID userId = jwtTokenProvider.getUserIdFromToken(token);
            System.out.println(userId);
            // ... ваша логика валидации

            attributes.put("userId", userId.toString());
            return true;
        } catch (Exception e) {
            //System.err.println("Error in Handshake: " + e.getMessage());
            //e.printStackTrace();
            return false;
        }
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @org.jspecify.annotations.Nullable Exception exception) {

    }
}

