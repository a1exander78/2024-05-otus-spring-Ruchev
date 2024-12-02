package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.dto.user.UserDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.model.Authority;
import ru.otus.project.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    UserInfoDto toInfoDto(User user);

    UserNoPasswordDto toNoPasswordDto(User user);

    List<AuthorityDto> convertAuthorities(List<Authority> authorities);
}
