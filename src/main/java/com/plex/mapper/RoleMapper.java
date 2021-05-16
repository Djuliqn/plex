package com.plex.mapper;

import com.plex.dto.RoleDto;
import com.plex.model.mysql.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto map(Role role);

    Role map(RoleDto roleDto);
}
