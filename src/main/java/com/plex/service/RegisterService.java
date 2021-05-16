package com.plex.service;

import com.plex.dto.RegisterRequest;
import com.plex.dto.RoleDto;
import com.plex.dto.UserDto;
import com.plex.mapper.RoleMapper;
import com.plex.model.mysql.Role;
import com.plex.model.mysql.User;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class RegisterService {

    private final UserService userService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserService userService, RoleService roleService,
                           RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto register(@NonNull RegisterRequest registerDto) {
        RoleDto roleDto = roleService.findByAuthority("USER");
        Role role = roleMapper.map(roleDto);
        String encodedPass = passwordEncoder.encode(registerDto.getPassword());
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPass)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        return userService.save(user);
    }
}
