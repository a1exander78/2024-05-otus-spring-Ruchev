package ru.otus.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.project.dto.cart.CartDto;
import ru.otus.project.dto.cart.CartUserDto;
import ru.otus.project.dto.cart.CartStatusBagsDto;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.model.Bag;
import ru.otus.project.model.Cart;
import ru.otus.project.model.Status;
import ru.otus.project.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(source = "cartStatus.id", target = "cartStatusId")
    CartUserDto toCartUserDto(Cart cart);

    @Mapping(source = "cart.user.id", target = "userId")
    CartStatusBagsDto toCartStatusBagsDto(Cart cart);

    StatusDto convertStatus(Status status);

    UserInfoDto convertUser(User user);

    List<BagDto> convertBags(List<Bag> bags);
}
