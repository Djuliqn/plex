package com.plex.configuration.security.jwt.provider;

import com.plex.exception.RecordNotFoundException;
import com.plex.model.mysql.User;
import com.plex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!StringUtils.hasText(authentication.getName()) || Objects.isNull(authentication.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(null, null, null);
        }

        String usernameOrEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        BadCredentialsException badCredentialsException = new BadCredentialsException("Invalid credentials.");
        User user;
        try {
            user = userService.loadUserByUsernameOrEmail(usernameOrEmail);
        } catch (RecordNotFoundException ex) {
            throw badCredentialsException;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw badCredentialsException;
        }

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
