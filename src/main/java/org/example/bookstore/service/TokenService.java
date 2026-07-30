package org.example.bookstore.service;

import org.example.bookstore.config.SecurityConfig;
import org.example.bookstore.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;

    public TokenService(
            JwtEncoder jwtEncoder,
            @Value("${security.jwt.expiration}") Duration expiration
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    public TokenResponse createToken(Authentication authentication) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expiration);
        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .toList();

        var claims = JwtClaimsSet.builder()
                .issuer(SecurityConfig.ISSUER)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", expiresAt);
    }
}
