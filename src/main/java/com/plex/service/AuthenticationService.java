package com.plex.service;

import com.plex.dto.AuthRequest;
import com.plex.dto.UserDto;
import com.plex.model.mysql.Role;
import com.plex.model.mysql.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserDto authenticate(@NotNull AuthRequest authRequest) {

        if (!StringUtils.hasText(authRequest.getUsernameOrEmail())
                || !StringUtils.hasText(authRequest.getPassword())) {
            throw new BadCredentialsException("Credentials should not be empty.");
        }
        
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User user = (User) authenticate.getPrincipal();

        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Invalid credentials.");
        }

        Set<String> authorities = user.getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .authorities(authorities)
                .build();
    }
}
