package com.przemarcz.auth.mapper;

import com.przemarcz.auth.dto.RegisterPersonalData;
import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.model.User;
import com.przemarcz.auth.model.enums.RoleName;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "login", ignore = true)
    UserDto toUserWorkerDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "password", source = "user.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "baseRole", source = "role")
    User toUser(RegisterUser user, RoleName role);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void addPersonalData(@MappingTarget User user, RegisterPersonalData registerPersonalData);

}
