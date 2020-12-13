package com.przemarcz.auth.mapper;

import com.przemarcz.auth.model.User;
import org.mapstruct.*;

import static com.przemarcz.auth.dto.UserDto.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    WorkerResponse toWorkerResponse(User user);

    UserResponse toUserResponse(User user);

    @Mapping(target = "password", source = "user.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "login", expression = "java(user.getLogin().toLowerCase())")
    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    User toUser(RegisterUserRequest user);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest userDto);
}
