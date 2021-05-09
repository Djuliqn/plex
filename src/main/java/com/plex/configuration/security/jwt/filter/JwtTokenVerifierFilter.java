package com.plex.configuration.security.jwt.filter;

import com.plex.configuration.security.jwt.util.JwtUtil;
import com.plex.exception.TokenExpiredException;
import com.plex.model.mysql.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenVerifierFilter.class);

    private final JwtUtil jwtUtil;

    public JwtTokenVerifierFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenFromRequest = jwtUtil.getTokenFromRequest(request);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        if (tokenFromRequest.isEmpty()) {
            logger.warn("Token from request {} is empty", request);
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(tokenFromRequest);
        String email = jwtUtil.getEmail(tokenFromRequest);
        if (username.isEmpty() || email.isEmpty()) {
            logger.error("There is something wrong with the token. {}", tokenFromRequest);
            filterChain.doFilter(request, response);
        }

        if (jwtUtil.isTokenExpired(tokenFromRequest)) {
            throw new TokenExpiredException(String.format("Token has expired. Expiration time: %s",
                    jwtUtil.getExpirationDate(tokenFromRequest).toString()));
        }

        Set<String> authorities = jwtUtil.getAuthorities(tokenFromRequest);
        Set<Role> roles = authorities.stream()
                .map(authority -> Role.builder().authority(authority).build())
                .collect(Collectors.toSet());

        authenticationToken = new UsernamePasswordAuthenticationToken(username, null, roles);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
