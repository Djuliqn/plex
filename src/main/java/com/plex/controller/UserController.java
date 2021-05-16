package com.plex.controller;

import com.plex.dto.UserDto;
import com.plex.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }
}
