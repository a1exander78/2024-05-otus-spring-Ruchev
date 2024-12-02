package ru.otus.project.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.dto.user.UserInfoDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private long id;

    private StatusDto cartStatus;

    private UserInfoDto user;

    private List<BagDto> bags;
}
