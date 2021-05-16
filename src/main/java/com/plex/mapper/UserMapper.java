package com.plex.mapper;

import com.plex.dto.UserDto;
import com.plex.model.mysql.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuthorityRoleMapper.class)
public interface UserMapper {

    User map(UserDto userDto);

    UserDto map(User user);
}
