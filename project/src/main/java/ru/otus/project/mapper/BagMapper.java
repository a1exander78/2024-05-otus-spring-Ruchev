package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.model.Bag;

@Mapper(componentModel = "spring")
public interface BagMapper {
    BagDto toDto(Bag bag);
}
