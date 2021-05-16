package com.plex.service;

import com.plex.dto.RoleDto;
import com.plex.exception.RecordNotFoundException;
import com.plex.mapper.RoleMapper;
import com.plex.model.mysql.Role;
import com.plex.repository.mysql.RoleRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);


    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public RoleDto findByAuthority(@NonNull String authority) {
        Role role = roleRepository.findByAuthority(authority)
                .orElseThrow(() -> {
                    logger.error("Role with authority: {} not found", authority);
                    return new RecordNotFoundException(String.format("Role not found with authority: %s", authority));
                });

        return roleMapper.map(role);
    }

    public Set<RoleDto> findByAuthorities(@NonNull Set<String> authorities) {
        return roleRepository.findByAuthorityIn(authorities)
                .stream()
                .map(roleMapper::map)
                .collect(Collectors.toSet());
    }
}
