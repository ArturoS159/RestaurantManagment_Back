package com.przemarcz.auth.mapper;

import com.przemarcz.auth.dto.RegisterUser;
import com.przemarcz.auth.dto.UserDto;
import com.przemarcz.auth.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "login", ignore = true)
    UserDto toUserWorkerDto(User user);

    UserDto toUserDto(User user);

    @Mapping(target = "password", source = "user.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "login", expression = "java(user.getLogin().toLowerCase())")
    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    @Mapping(target = "forename", ignore = true)
    @Mapping(target = "surname", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "postCode", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "houseNumber", ignore = true)
    @Mapping(target = "restaurantRoles", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toUser(RegisterUser user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserDto userDto);
}
