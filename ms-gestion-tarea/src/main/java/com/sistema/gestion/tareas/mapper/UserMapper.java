package com.sistema.gestion.tareas.mapper;

import com.sistema.gestion.tareas.dto.UserDto;
import com.sistema.gestion.tareas.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    List<UserDto> toUserDtoList(List<User> userList);

    List<User> toUserList(List<UserDto> userDtoList);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromUserDto(UserDto userDto, @MappingTarget User user);
}
