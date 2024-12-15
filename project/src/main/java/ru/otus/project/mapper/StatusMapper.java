package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.model.Status;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDto toDto(Status status);
}
