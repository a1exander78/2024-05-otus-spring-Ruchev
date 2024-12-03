package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.dto.user.UserDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.dto.user.UserRequestDto;
import ru.otus.project.dto.user.UserAuthoritiesDto;
import ru.otus.project.model.Authority;
import ru.otus.project.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    UserInfoDto toInfoDto(User user);

    UserNoPasswordDto toNoPasswordDto(User user);

    @Mapping(source = "password", target = "rawPassword")
    @Mapping(source = "user.address.district", target = "district")
    @Mapping(source = "user.address.street", target = "street")
    @Mapping(source = "user.address.streetNumber", target = "streetNumber")
    @Mapping(source = "user.address.flatNumber", target = "flatNumber")
    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "mapAuthorities")
    UserRequestDto toAddressDto(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "mapAuthorities")
    UserAuthoritiesDto toAuthoritiesDto(User user);

    AuthorityDto convertAuthority(Authority authority);

    @Named("mapAuthorities")
    default List<Long> convertAuthoritiesToLong(List<Authority> authorities) {
        return authorities.stream().map(Authority::getId).toList();
    }
}
