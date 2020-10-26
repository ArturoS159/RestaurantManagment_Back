package com.przemarcz.auth.mapper;

import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "login", ignore = true)
    UserDto toUserWorkerDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "password", source = "userDto.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "baseRole", source = "roleName")
    User toUser(UserDto userDto, RoleName roleName);

}
