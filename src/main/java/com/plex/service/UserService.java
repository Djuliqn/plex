package com.plex.service;

import com.plex.dto.RoleDto;
import com.plex.dto.UserDto;
import com.plex.exception.RecordAlreadyExistsException;
import com.plex.exception.RecordNotFoundException;
import com.plex.mapper.RoleMapper;
import com.plex.mapper.UserMapper;
import com.plex.model.mysql.Role;
import com.plex.model.mysql.User;
import com.plex.repository.mysql.UserRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserMapper userMapper, RoleMapper roleMapper,
                       UserRepository userRepository, RoleService roleService) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<UserDto> findAll() {
        logger.info("Fetching data for all users...");
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .collect(Collectors.toList());
    }

    public UserDto findById(@NonNull Long id) {
        logger.info(String.format("Fetching data for user with id: %s", id));
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id: {} not found", id);
                    return new RecordNotFoundException(String.format("User not found with id: %s", id));
                });

        return userMapper.map(user);
    }

    public User loadUserByUsernameOrEmail(@NonNull String usernameOrEmail) {
        logger.info("Fetching user by username or email: {}", usernameOrEmail);
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User with username or email: {} not found", usernameOrEmail);
                    return new RecordNotFoundException(String.format("User not found with: %s", usernameOrEmail));
                });
    }

    public UserDto save(@NonNull User user) {
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            logger.error("Failed to save user. It already exists: {}", user);
            throw new RecordAlreadyExistsException(String.format("User already exists: %s", user));
        }

        logger.info("User saved: {}", savedUser);
        return userMapper.map(savedUser);
    }

    public UserDto update(@NonNull Long id, @NonNull UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id: {} not found", id);
                    return new RecordNotFoundException(String.format("User not found with id: %s", id));
                });

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        Set<RoleDto> roleDtos = roleService.findByAuthorities(userDto.getAuthorities());
        Set<Role> roles = roleDtos.stream()
                .map(roleMapper::map)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return this.save(user);
    }


    public void delete(@NonNull Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("User with id: {} doesn't exist", id);
            throw new RecordNotFoundException(String.format("User with id: %s doesn't exist.", id));
        }

        logger.info("User deleted successfully with id: {}", id);
    }

}
