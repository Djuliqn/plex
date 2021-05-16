package com.plex.mapper;

import com.plex.model.mysql.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorityRoleMapper {

    @Mapping(target = "authority", source = "authority")
    Role map(String authority);

    default String toString(Role role) {
        return role != null ? role.getAuthority() : null;
    }
}
