package com.plex.controller;

import com.plex.configuration.security.jwt.util.JwtUtil;
import com.plex.dto.AuthRequest;
import com.plex.dto.Token;
import com.plex.dto.UserDto;
import com.plex.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Token> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        UserDto authenticatedUser = authenticationService.authenticate(authRequest);
        return ResponseEntity.ok(jwtUtil.generateToken(authenticatedUser));
    }
}
