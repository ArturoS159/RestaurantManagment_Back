package com.przemarcz.auth.domain.mapper;

import com.przemarcz.auth.domain.model.User;
import org.mapstruct.*;

import java.util.UUID;

import static com.przemarcz.auth.domain.dto.UserDto.*;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class, imports = UUID.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "password", source = "user.password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "login", expression = "java(user.getLogin().toLowerCase())")
    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    User toUser(RegisterUserRequest user);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UpdateUserRequest userDto);

    WorkerResponse toWorkerResponse(User user);

    UserResponse toUserResponse(User user);
}
