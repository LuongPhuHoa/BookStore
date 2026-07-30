package org.example.bookstore.controller;

import jakarta.validation.Valid;
import org.example.bookstore.dto.LoginRequest;
import org.example.bookstore.dto.TokenResponse;
import org.example.bookstore.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        var credentials = UsernamePasswordAuthenticationToken.unauthenticated(
                request.username(),
                request.password());
        var authentication = authenticationManager.authenticate(credentials);
        return tokenService.createToken(authentication);
    }
}
