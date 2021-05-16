package com.plex.controller;

import com.plex.dto.RegisterRequest;
import com.plex.dto.UserDto;
import com.plex.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(registerService.register(request));
    }
}
