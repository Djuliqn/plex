package com.plex.configuration.security.jwt.util;

import com.plex.dto.Token;
import com.plex.dto.UserDto;
import com.plex.model.mysql.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String AUTHORITIES = "authorities";
    private static final String EMAIL = "email";

    private final Long tokenExpiration;
    private final Key secretKey;

    public JwtUtil(@Value(value = "${token.expiration}") Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public LocalDateTime getExpirationDate(String token) {
        return LocalDateTime.ofInstant(
                getClaimFromToken(token, Claims::getExpiration).toInstant(),
                ZoneId.systemDefault());
    }

    public String getEmail(String token) {
        return getClaimFromToken(token, claims -> claims.get(EMAIL, String.class));
    }

    public Set<String> getAuthorities(String token) {
        return new HashSet<>(getClaimFromToken(token, claims -> claims.get(AUTHORITIES, List.class)));
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }

        return Strings.EMPTY;
    }

    public boolean isTokenValid(String token, User user) {
        final String username = getUsername(token);
        final String email = getEmail(token);
        return username.equals(user.getUsername())
                && email.equals(user.getEmail())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDate(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    public Token generateToken(UserDto userDto) {
        Map<String, Object> claims = Map.of(
                AUTHORITIES, userDto.getAuthorities(),
                EMAIL, userDto.getEmail());

        String type = Header.JWT_TYPE;
        String accessToken = Jwts.builder()
                .setClaims(Jwts.claims(claims))
                .setSubject(userDto.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(tokenExpiration, ChronoUnit.SECONDS)))
                .setHeader(Map.of("typ", type))
                .signWith(secretKey)
                .compact();

        return Token.builder()
                .accessToken(accessToken)
                .tokenType(type)
                .build();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
