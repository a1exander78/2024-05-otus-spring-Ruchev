package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.model.Authority;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityDto toDto(Authority authority);
}
